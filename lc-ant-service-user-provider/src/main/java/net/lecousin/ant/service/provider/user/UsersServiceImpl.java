package net.lecousin.ant.service.provider.user;

import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.lecousin.ant.core.api.exceptions.ConflictException;
import net.lecousin.ant.core.security.Root;
import net.lecousin.ant.core.springboot.aop.Trace;
import net.lecousin.ant.core.springboot.service.provider.helper.AbstractCRUDMultiTenantService;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantServiceHelper;
import net.lecousin.ant.service.provider.user.db.UserEntity;
import net.lecousin.ant.service.provider.user.exceptions.UserNotFoundException;
import net.lecousin.ant.service.user.UsersService;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

@Service("userServiceProviderUsers")
@Primary
@Slf4j
public class UsersServiceImpl extends AbstractCRUDMultiTenantService<User, UserEntity> implements UsersService {
	
	public UsersServiceImpl(MultiTenantServiceHelper tenantHelper) {
		super(tenantHelper, UserServiceImpl.SERVICE_NAME);
	}
	
	@Override
	protected Class<User> dtoClass() {
		return User.class;
	}
	
	@Override
	protected Class<UserEntity> entityClass() {
		return UserEntity.class;
	}

	
	@Trace(service = UserServiceImpl.SERVICE_NAME)
	Mono<User> searchEmail(String email) {
		return tenantHelper.findOneAnyTenant(UserServiceImpl.SERVICE_NAME, UserEntity.class, UserEntity.FIELD_EMAIL.is(email))
		.map(entityToDto::apply)
		.switchIfEmpty(Mono.error(new UserNotFoundException(email)));
	}

	Mono<Void> createRootUserOnRootTenant(String rootTenantId, String rootRoleId) {
		String email = System.getenv("SERVICE_USER_INIT_ROOT_EMAIL");
		String password = System.getenv("SERVICE_USER_INIT_ROOT_PASSWORD");
		if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
			log.debug("No environnement variable SERVICE_USER_INIT_ROOT_EMAIL and SERVICE_USER_INIT_ROOT_PASSWORD set");
			return Mono.empty();
		}
		log.debug("Try to create root user on root tenant with email {}", email);
		return db(rootTenantId).flatMap(db -> {
			UserEntity user = new UserEntity();
			user.setTenantId(rootTenantId);
			user.setUsername(Root.AUTHORITY.toLowerCase());
			user.setEmail(email);
			user.setEmailVerified(false);
			user.setFirstName("Root");
			user.setLastName("Administrator");
			user.setRoles(List.of(rootRoleId));
			user.setPassword(Optional.of(hashPassword(password)));
			return db.create(user);
		})
		.onErrorComplete(ConflictException.class)
		.then();
	}
	
	Mono<Void> deleteAllUsersOnTenant(String tenantId) {
		log.info("Delete all users on tenant {}", tenantId);
		return db(tenantId).flatMap(db -> 
			db.delete(UserEntity.class, MultiTenantEntity.FIELD_TENANT_ID.is(tenantId))
			.then(db.deleteTenantTable(UserEntity.class, tenantId))
		);
	}
	
	static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}
	
}
