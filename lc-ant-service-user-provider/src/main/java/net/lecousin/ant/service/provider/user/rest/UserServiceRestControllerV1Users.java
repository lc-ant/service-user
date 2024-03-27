package net.lecousin.ant.service.provider.user.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.core.api.PageRequest;
import net.lecousin.ant.core.api.PageResponse;
import net.lecousin.ant.core.expression.Expression;
import net.lecousin.ant.service.provider.user.UsersServiceImpl;
import net.lecousin.ant.service.user.UsersService;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user/v1/users/{tenantId}")
@RequiredArgsConstructor
public class UserServiceRestControllerV1Users implements UsersService {
	
	private final UsersServiceImpl service;

	@Override
	@PostMapping("/_search")
	public Mono<PageResponse<User>> search(@PathVariable("tenantId") String tenantId, @RequestBody(required = false) Expression<Boolean> criteria, PageRequest pageRequest) {
		return service.search(tenantId, criteria, pageRequest);
	}

	@Override
	@PostMapping
	public Mono<User> create(@PathVariable("tenantId") String tenantId, @RequestBody User dto) {
		return service.create(tenantId, dto);
	}

	@Override
	@PutMapping
	public Mono<User> update(@PathVariable("tenantId") String tenantId, @RequestBody User dto) {
		return service.update(tenantId, dto);
	}

	@Override
	@DeleteMapping("/{userId}")
	public Mono<Void> delete(@PathVariable("tenantId") String tenantId, @PathVariable("userId") String id) {
		return service.delete(tenantId, id);
	}
	
}
