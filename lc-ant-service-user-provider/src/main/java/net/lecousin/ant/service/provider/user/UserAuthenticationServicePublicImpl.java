package net.lecousin.ant.service.provider.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.core.api.PageResponse;
import net.lecousin.ant.core.api.exceptions.UnauthorizedException;
import net.lecousin.ant.core.api.traceability.Traceability;
import net.lecousin.ant.core.expression.impl.ConditionAnd;
import net.lecousin.ant.core.mapping.Mappers;
import net.lecousin.ant.core.security.Grant;
import net.lecousin.ant.core.security.LcAntSecurity;
import net.lecousin.ant.core.security.Root;
import net.lecousin.ant.core.springboot.aop.Trace;
import net.lecousin.ant.core.springboot.security.JwtRequest;
import net.lecousin.ant.core.springboot.security.JwtResponse;
import net.lecousin.ant.core.springboot.service.provider.helper.DefaultServiceAuthenticationProvider;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantServiceHelper;
import net.lecousin.ant.core.springboot.service.provider.info.ServiceInfo;
import net.lecousin.ant.service.provider.user.db.RoleEntity;
import net.lecousin.ant.service.provider.user.db.UserEntity;
import net.lecousin.ant.service.security.SecurityService;
import net.lecousin.ant.service.security.dto.AuthenticationWithAuthoritiesRequest;
import net.lecousin.ant.service.tenant.TenantPublicService;
import net.lecousin.ant.service.user.UserAuthenticationPublicService;
import net.lecousin.ant.service.user.dto.AuthenticatedUserResponse;
import net.lecousin.ant.service.user.dto.User;
import net.lecousin.commons.reactive.MonoUtils;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;
import reactor.util.function.Tuples;

@Service("userServiceProviderUserPublicAuthentication")
@Primary
@RequiredArgsConstructor
public class UserAuthenticationServicePublicImpl implements UserAuthenticationPublicService, InitializingBean {

	private final SecurityService securityService;
	private final DefaultServiceAuthenticationProvider serviceAuthService;
	private final MultiTenantServiceHelper multiTenantHelper;
	private final UserServiceProperties properties;
	private final UsersServiceImpl users;
	private final TenantPublicService tenantPublicService;
	private final ServiceInfo serviceInfo;
	
	private Function<UserEntity, User> userEntityToDto;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		userEntityToDto = Mappers.createMapper(UserEntity.class, User.class);
	}
	
	
	@Override
	@Trace(service = UserServiceImpl.SERVICE_NAME)
	public Mono<AuthenticatedUserResponse> authenticateUserByUsernameAndPassword(String tenantId, String username, String password) {
		return Mono.deferContextual(ctx -> {
		var traceOpt = Traceability.fromContext(ctx);
		traceOpt.ifPresent(trace -> {
			trace.setTenantId(tenantId);
			trace.setUsername("user:" + username);
		});
		return MonoUtils.zipParallel(
			tenantPublicService.findById(tenantId),
			multiTenantHelper.getDb(UserServiceImpl.SERVICE_NAME, tenantId)
			.flatMap(db ->
				db.find(UserEntity.class)
				.where(new ConditionAnd(
					MultiTenantEntity.FIELD_TENANT_ID.is(tenantId),
					UserEntity.FIELD_USERNAME.is(username),
					UserEntity.FIELD_PASSWORD.is(UsersServiceImpl.hashPassword(password))
				))
				.executeSingle()
				.zipWhen(user -> user.getRoles().isEmpty() ? Mono.just(List.<RoleEntity>of()) :
					db.find(RoleEntity.class)
					.where(RoleEntity.FIELD_ID.in(user.getRoles()))
					.execute()
					.map(PageResponse::getData)
				)
				.flatMap(userAndRoles -> {
					List<String> authorities = new LinkedList<>();
					boolean isRoot = userAndRoles.getT2().stream().anyMatch(r -> Root.AUTHORITY.equals(r.getName()));
					if (isRoot) authorities.add(Root.AUTHORITY);
					authorities.addAll(
						userAndRoles.getT2().stream()
						.flatMap(role -> role.getAuthorities().stream())
						.distinct()
						.toList()
					);
					return serviceInfo.resolveAllPermissions(authorities)
					.map(allPermissions -> {
						User user = userEntityToDto.apply(userAndRoles.getT1());
						user.setAuthorities(allPermissions.stream().map(Grant::toAuthority).distinct().toList());
						return Tuples.of(user, isRoot);
					});
				})
			)
		)
		.switchIfEmpty(Mono.error(new UnauthorizedException()))
		.flatMap(tenantAndUserAndRootRole -> {
			if (!tenantAndUserAndRootRole.getT1().isActive()) return Mono.error(new UnauthorizedException());
			return serviceAuthService.executeMonoAs(UserServiceImpl.SERVICE_NAME, () ->
				securityService.internalAuth().authenticated(AuthenticationWithAuthoritiesRequest.builder()
					.tenantId(Optional.of(tenantId))
					.subjectType(LcAntSecurity.SUBJECT_TYPE_USER)
					.subjectId(username)
					.authorities(tenantAndUserAndRootRole.getT2().getT1().getAuthorities())
					.root(Root.AUTHORITY.equals(tenantAndUserAndRootRole.getT1().getPublicId()) && tenantAndUserAndRootRole.getT2().getT2())
					.tokenDuration(properties.getUserTokenDuration())
					.renewTokenDuration(properties.getUserRenewTokenDuration())
					.build()
				)
			)
			.map(jwt -> new AuthenticatedUserResponse(jwt, tenantAndUserAndRootRole.getT2().getT1()));
		});});
	}

	
	@Override
	public Mono<User> searchEmail(String email) {
		return users.searchEmail(email)
		.map(user -> {
			User publicUser = new User();
			publicUser.setEmail(user.getEmail());
			publicUser.setUsername(user.getUsername());
			publicUser.setTenantId(user.getTenantId());
			return publicUser;
		});
	}

	@Override
	@Trace(service = UserServiceImpl.SERVICE_NAME)
	public Mono<JwtResponse> refreshToken(String tenantId, JwtRequest tokens) {
		return serviceAuthService.executeMonoAs(UserServiceImpl.SERVICE_NAME, () ->
			// TODO we must get again permissions, isRoot, and if tenant is still active, so if permissions changed it is refreshed
			securityService.internalAuth().renewToken(tokens)
		)
		.flatMap(response -> Mono.deferContextual(ctx -> {
			addTraceabilityContextFromToken(tenantId, response.getAccessToken(), ctx);
			return Mono.just(response);
		}));
	}
	
	@Override
	@Trace(service = UserServiceImpl.SERVICE_NAME)
	public Mono<Void> closeToken(String tenantId, JwtRequest tokens) {
		return serviceAuthService.executeMonoAs(UserServiceImpl.SERVICE_NAME, () ->
			securityService.internalAuth().closeToken(tokens)
		);
	}
	
	private void addTraceabilityContextFromToken(String tenantId, String accessToken, ContextView ctx) {
		Traceability.fromContext(ctx).ifPresent(trace -> {
			trace.setTenantId(tenantId);
			trace.setUsername(JWT.decode(accessToken).getSubject());
		});
	}

}
