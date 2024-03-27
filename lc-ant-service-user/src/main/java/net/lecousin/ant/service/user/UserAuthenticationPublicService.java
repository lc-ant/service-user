package net.lecousin.ant.service.user;

import net.lecousin.ant.core.springboot.security.JwtRequest;
import net.lecousin.ant.core.springboot.security.JwtResponse;
import net.lecousin.ant.service.user.dto.AuthenticatedUserResponse;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

public interface UserAuthenticationPublicService {

	Mono<AuthenticatedUserResponse> authenticateUserByUsernameAndPassword(String tenantId, String username, String password);
	
	Mono<User> searchEmail(String email);
	
	Mono<JwtResponse> refreshToken(String tenantId, JwtRequest tokens);
	
	Mono<Void> closeToken(String tenantId, JwtRequest tokens);

}
