package net.lecousin.ant.service.client.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import net.lecousin.ant.core.springboot.security.JwtRequest;
import net.lecousin.ant.core.springboot.security.JwtResponse;
import net.lecousin.ant.core.springboot.service.client.LcAntServiceClient;
import net.lecousin.ant.service.user.UserAuthenticationPublicService;
import net.lecousin.ant.service.user.dto.AuthenticatedUserResponse;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

@HttpExchange("/public-api/user/v1/auth")
@LcAntServiceClient(serviceName = "user", serviceUrl = "${lc-ant.services.user:user-service}", qualifier = "userServiceRestClientV1PublicAuth")
public interface UserServiceClientV1UserAuthenticationPublicService extends UserAuthenticationPublicService {

	@Override
	@PostExchange("/{tenantId}/user/{username}")
	Mono<AuthenticatedUserResponse> authenticateUserByUsernameAndPassword(@PathVariable("tenantId") String tenantId, @PathVariable("username") String username, @RequestBody String password);

	
	@Override
	@GetExchange("/_searchEmail")
	Mono<User> searchEmail(@RequestParam("email") String email);
	
	@Override
	@PostExchange("/{tenantId}/refreshToken")
	Mono<JwtResponse> refreshToken(@PathVariable("tenantId")String tenantId, @RequestBody JwtRequest tokens);

	
	@Override
	@PostExchange("/{tenantId}/closeToken")
	Mono<Void> closeToken(@PathVariable("tenantId")String tenantId, @RequestBody JwtRequest tokens);

}
