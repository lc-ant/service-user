package net.lecousin.ant.service.provider.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.lecousin.ant.core.api.exceptions.ConflictException;
import net.lecousin.ant.core.expression.impl.ConditionAnd;
import net.lecousin.ant.core.security.Root;
import net.lecousin.ant.core.springboot.service.provider.helper.AbstractCRUDMultiTenantService;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantServiceHelper;
import net.lecousin.ant.service.provider.user.db.RoleEntity;
import net.lecousin.ant.service.provider.user.exceptions.RoleRootReservedException;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.dto.Role;
import reactor.core.publisher.Mono;

@Service("userServiceProviderRoles")
@Primary
@Slf4j
public class RolesServiceImpl extends AbstractCRUDMultiTenantService<Role, RoleEntity> implements RolesService {

	public RolesServiceImpl(MultiTenantServiceHelper tenantHelper) {
		super(tenantHelper, UserServiceImpl.SERVICE_NAME);
	}

	@Override
	protected Class<Role> dtoClass() {
		return Role.class;
	}
	
	@Override
	protected Class<RoleEntity> entityClass() {
		return RoleEntity.class;
	}
	
	@Override
	public Mono<Role> create(String tenantId, Role dto) {
		if (Root.AUTHORITY.equalsIgnoreCase(dto.getName()))
			return Mono.error(new RoleRootReservedException());
		return super.create(tenantId, dto);
	}
	
	@Override
	public Mono<Role> update(String tenantId, Role dto) {
		if (Root.AUTHORITY.equalsIgnoreCase(dto.getName()))
			return Mono.error(new RoleRootReservedException());
		return super.update(tenantId, dto);
	}
	
	Mono<String> createRootRoleOnRootTenant(String rootTenantId) {
		log.info("Try to create root role on root tenant");
		RoleEntity entity = new RoleEntity();
		entity.setTenantId(rootTenantId);
		entity.setName(Root.AUTHORITY);
		return db(rootTenantId)
			.flatMap(db ->
				db.create(entity)
				.onErrorResume(ConflictException.class, e ->
					db.find(RoleEntity.class)
					.where(new ConditionAnd(MultiTenantEntity.FIELD_TENANT_ID.is(rootTenantId), RoleEntity.FIELD_NAME.is(Root.AUTHORITY)))
					.executeSingle()
				)
			)
			.map(RoleEntity::getId);
	}
	
	Mono<Void> deleteAllRolesOnTenant(String tenantId) {
		log.info("Delete all roles for tenant {}", tenantId);
		return db(tenantId).flatMap(db -> 
			db.delete(RoleEntity.class, MultiTenantEntity.FIELD_TENANT_ID.is(tenantId))
			.then(db.deleteTenantTable(RoleEntity.class, tenantId))
		);
	}
	
}
