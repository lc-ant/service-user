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
import net.lecousin.ant.service.user.UsersService;
import net.lecousin.ant.service.user.dto.User;
import reactor.core.publisher.Mono;

@HttpExchange("/api/user/v1/users/{tenantId}")
@LcAntServiceClient(serviceName = "user", serviceUrl = "${lc-ant.services.user:user-service}", qualifier = "userServiceRestClientV1Users")
public interface UserServiceClientV1UsersService extends UsersService {

	@Override
	@PostExchange("/_search")
	Mono<PageResponse<User>> search(@PathVariable("tenantId") String tenantId, @RequestBody(required = false) Expression<Boolean> criteria, PageRequest pageRequest);

	@Override
	@PostExchange
	Mono<User> create(@PathVariable("tenantId") String tenantId, @RequestBody User dto);

	@Override
	@PutExchange
	Mono<User> update(@PathVariable("tenantId") String tenantId, @RequestBody User dto);

	@Override
	@DeleteExchange("/{userId}")
	Mono<Void> delete(@PathVariable("tenantId") String tenantId, @PathVariable("userId") String id);

}
