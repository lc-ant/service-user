package net.lecousin.ant.service.provider.user.rest;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.service.user.UserService;

@Service("userServiceProviderRestController")
@RequiredArgsConstructor
public class UserServiceRestControllerV1 implements UserService {

	private final UserServiceRestControllerV1Users users;
	private final UserServiceRestControllerV1Roles roles;
	private final UserServiceRestControllerV1PublicAuth authPublic;
	
	@Override
	public UserServiceRestControllerV1Users users() {
		return users;
	}
	
	@Override
	public UserServiceRestControllerV1Roles roles() {
		return roles;
	}
	
	@Override
	public UserServiceRestControllerV1PublicAuth authPublic() {
		return authPublic;
	}
	
}
