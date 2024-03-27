package net.lecousin.ant.service.provider.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.lecousin.ant.core.springboot.service.provider.helper.AbstractCRUDMultiTenantService;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantServiceHelper;
import net.lecousin.ant.service.provider.user.db.UserEntity;
import net.lecousin.ant.service.user.UsersService;
import net.lecousin.ant.service.user.dto.User;

@Service("userServiceProviderUsers")
@Primary
public class UsersServiceImpl extends AbstractCRUDMultiTenantService<User, UserEntity> implements UsersService {

	public UsersServiceImpl(MultiTenantServiceHelper tenantHelper) {
		super(tenantHelper, UserServiceConstants.SERVICE_NAME);
	}

	@Override
	protected Class<User> dtoClass() {
		return User.class;
	}
	
	@Override
	protected Class<UserEntity> entityClass() {
		return UserEntity.class;
	}
	
}
