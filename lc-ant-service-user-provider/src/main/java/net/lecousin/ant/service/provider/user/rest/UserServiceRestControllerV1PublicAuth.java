package net.lecousin.ant.service.provider.user.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.core.springboot.security.JwtRequest;
import net.lecousin.ant.core.springboot.security.JwtResponse;
import net.lecousin.ant.service.provider.user.UserAuthenticationServicePublicImpl;
import net.lecousin.ant.service.user.UserAuthenticationPublicService;
import net.lecousin.ant.service.user.dto.AuthenticatedUserResponse;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public-api/user/v1/auth")
@RequiredArgsConstructor
public class UserServiceRestControllerV1PublicAuth implements UserAuthenticationPublicService {

	private final UserAuthenticationServicePublicImpl service;
	
	@Override
	@PostMapping("/{tenantId}/user/{username}")
	public Mono<AuthenticatedUserResponse> authenticateUserByUsernameAndPassword(@PathVariable("tenantId") String tenantId, @PathVariable("username") String username, @RequestBody String password) {
		return service.authenticateUserByUsernameAndPassword(tenantId, username, password);
	}
	
	@GetMapping("/_searchEmail")
	@Override
	public Mono<User> searchEmail(@RequestParam("email") String email) {
		return service.searchEmail(email);
	}

	@Override
	@PostMapping("/{tenantId}/refreshToken")
	public Mono<JwtResponse> refreshToken(@PathVariable("tenantId") String tenantId, @RequestBody JwtRequest tokens) {
		return service.refreshToken(tenantId, tokens);
	}

	@Override
	@PostMapping("/{tenantId}/closeToken")
	public Mono<Void> closeToken(@PathVariable("tenantId") String tenantId, @RequestBody JwtRequest tokens) {
		return service.closeToken(tenantId, tokens);
	}
	
}
