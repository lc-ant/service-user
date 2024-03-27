package net.lecousin.ant.service.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.lecousin.ant.service.user.UserService;

@Service("userServiceRestClientV1")
public class UserServiceClientV1 implements UserService {
	
	private @Autowired @Lazy  UserServiceClientV1UsersService users;
	private @Autowired @Lazy  UserServiceClientV1RolesService roles;
	private @Autowired @Lazy  UserServiceClientV1UserAuthenticationPublicService authPublic;

	@Override
	public UserServiceClientV1UsersService users() {
		return users;
	}
	
	@Override
	public UserServiceClientV1RolesService roles() {
		return roles;
	}
	
	@Override
	public UserServiceClientV1UserAuthenticationPublicService authPublic() {
		return authPublic;
	}
	
}
