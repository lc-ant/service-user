package net.lecousin.ant.service.client.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import net.lecousin.ant.core.api.PageRequest;
import net.lecousin.ant.core.api.PageResponse;
import net.lecousin.ant.core.expression.Expression;
import net.lecousin.ant.core.springboot.service.client.LcAntServiceClient;
import net.lecousin.ant.service.user.RolesService;
import net.lecousin.ant.service.user.dto.Role;
import reactor.core.publisher.Mono;

@HttpExchange("/api/user/v1/roles/{tenantId}")
@LcAntServiceClient(serviceName = "user", serviceUrl = "${lc-ant.services.user:user-service}", qualifier = "userServiceRestClientV1Roles")
public interface UserServiceClientV1RolesService extends RolesService {

	@Override
	@PostExchange("/_search")
	Mono<PageResponse<Role>> search(@PathVariable("tenantId") String tenantId, @RequestBody(required = false) Expression<Boolean> criteria, PageRequest pageRequest);

	@Override
	@PostExchange
	Mono<Role> create(@PathVariable("tenantId") String tenantId, @RequestBody Role dto);

	@Override
	@PutExchange
	Mono<Role> update(@PathVariable("tenantId") String tenantId, @RequestBody Role dto);

	@Override
	@DeleteExchange("/{roleId}")
	Mono<Void> delete(@PathVariable("tenantId") String tenantId, @PathVariable("roleId") String id);

}
