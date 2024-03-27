package net.lecousin.ant.service.provider.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.UserAuthenticationService;
import net.lecousin.ant.service.user.UserService;
import net.lecousin.ant.service.user.UsersService;

@Service("userServiceProvider")
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UsersServiceImpl users;
	private final RolesServiceImpl roles;
	private final UserAuthenticationServiceImpl auth;
	
	@Override
	public UsersService users() {
		return users;
	}
	
	@Override
	public RolesService roles() {
		return roles;
	}
	
	@Override
	public UserAuthenticationService auth() {
		return auth;
	}
	
}
