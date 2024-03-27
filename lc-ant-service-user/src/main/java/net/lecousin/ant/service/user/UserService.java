package net.lecousin.ant.service.user;

public interface UserService {

	UsersService users();
	
	RolesService roles();
	
	UserAuthenticationPublicService authPublic();
	
}
