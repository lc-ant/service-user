package net.lecousin.ant.service.provider.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.lecousin.ant.core.springboot.service.provider.helper.AbstractCRUDMultiTenantService;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantServiceHelper;
import net.lecousin.ant.service.provider.user.db.RoleEntity;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.dto.Role;

@Service("userServiceProviderRoles")
@Primary
public class RolesServiceImpl extends AbstractCRUDMultiTenantService<Role, RoleEntity> implements RolesService {

	public RolesServiceImpl(MultiTenantServiceHelper tenantHelper) {
		super(tenantHelper, UserServiceConstants.SERVICE_NAME);
	}

	@Override
	protected Class<Role> dtoClass() {
		return Role.class;
	}
	
	@Override
	protected Class<RoleEntity> entityClass() {
		return RoleEntity.class;
	}
	
}
