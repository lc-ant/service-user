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
import net.lecousin.ant.service.provider.user.RolesServiceImpl;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.dto.Role;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user/v1/roles/{tenantId}")
@RequiredArgsConstructor
public class UserServiceRestControllerV1Roles implements RolesService {
	
	private final RolesServiceImpl service;

	@Override
	@PostMapping("/_search")
	public Mono<PageResponse<Role>> search(@PathVariable("tenantId") String tenantId, @RequestBody(required = false) Expression<Boolean> criteria, PageRequest pageRequest) {
		return service.search(tenantId, criteria, pageRequest);
	}

	@Override
	@PostMapping
	public Mono<Role> create(@PathVariable("tenantId") String tenantId, @RequestBody Role dto) {
		return service.create(tenantId, dto);
	}

	@Override
	@PutMapping
	public Mono<Role> update(@PathVariable("tenantId") String tenantId, @RequestBody Role dto) {
		return service.update(tenantId, dto);
	}

	@Override
	@DeleteMapping("/{roleId}")
	public Mono<Void> delete(@PathVariable("tenantId") String tenantId, @PathVariable("roleId") String id) {
		return service.delete(tenantId, id);
	}

}
