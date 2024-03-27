package net.lecousin.ant.service.provider.user;

import java.util.Collections;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lecousin.ant.core.security.NodePermissionDeclaration;
import net.lecousin.ant.core.security.PermissionDeclaration;
import net.lecousin.ant.core.security.Root;
import net.lecousin.ant.core.springboot.messaging.ApiDataChangeEvent;
import net.lecousin.ant.core.springboot.messaging.ApiDataChangeEventUnicastListener;
import net.lecousin.ant.core.springboot.service.provider.LcAntServiceProvider;
import net.lecousin.ant.service.security.SecurityService;
import net.lecousin.ant.service.tenant.TenantPublicService;
import net.lecousin.ant.service.tenant.TenantService;
import net.lecousin.ant.service.tenant.dto.Tenant;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.UserAuthenticationPublicService;
import net.lecousin.ant.service.user.UserService;
import net.lecousin.ant.service.user.UsersService;
import net.lecousin.commons.reactive.MonoUtils;
import reactor.core.publisher.Mono;

@Service("userServiceProvider")
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, LcAntServiceProvider {

	public static final String SERVICE_NAME = "user";
	
	private final UsersServiceImpl users;
	private final RolesServiceImpl roles;
	private final UserAuthenticationServicePublicImpl authPublic;
	
	private final TenantService tenantService;
	private final TenantPublicService tenantPublicService;
	private final SecurityService securityService;
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}
	
	@Override
	public List<PermissionDeclaration> getServicePermissions() {
		return UserServicePermissions.ALL;
	}
	
	@Override
	public List<NodePermissionDeclaration> getServiceNodePermissions() {
		return Collections.emptyList();
	}
	
	@Override
	public List<Object> getDependencies() {
		return List.of(tenantService, securityService);
	}
	
	@Override
	public UsersService users() {
		return users;
	}
	
	@Override
	public RolesService roles() {
		return roles;
	}
	
	@Override
	public UserAuthenticationPublicService authPublic() {
		return authPublic;
	}
	
	@ApiDataChangeEventUnicastListener(service = SERVICE_NAME, name = "user-service", type = Tenant.class)
	public Mono<Void> tenantEvent(ApiDataChangeEvent event) {
		Tenant tenant = (Tenant) event.getData();
		log.debug("Tenant event received: {}", event);
		switch (event.getEventType()) {
		case CREATED:
			if (tenant.getPublicId().equals(Root.AUTHORITY.toLowerCase()))
				return roles.createRootRoleOnRootTenant(tenant.getId()).flatMap(roleId -> users.createRootUserOnRootTenant(tenant.getId(), roleId));
			return Mono.empty();
		case DELETED:
			return MonoUtils.zipVoidParallel(
				users.deleteAllUsersOnTenant(event.getDataId()),
				roles.deleteAllRolesOnTenant(event.getDataId()));
		default: return Mono.empty();
		}
	}
	
	@Override
	public Mono<Void> init(ConfigurableApplicationContext applicationContext) {
		return tenantPublicService.findByPublicId(Root.AUTHORITY)
		.flatMap(rootTenant ->
			roles.createRootRoleOnRootTenant(rootTenant.getId())
			.flatMap(roleId -> users.createRootUserOnRootTenant(rootTenant.getId(), roleId))
		);
	}
	
	@Override
	public Mono<Void> stop(ConfigurableApplicationContext applicationContext) {
		return Mono.empty();
	}
	
}
