package net.lecousin.ant.service.provider.user.security;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.lecousin.ant.core.springboot.security.Permission;
import net.lecousin.ant.service.provider.user.UserServiceConstants;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Permissions {

	public static final Permission READ = new Permission(UserServiceConstants.SERVICE_NAME, "read", List.of());
	public static final Permission UPDATE = new Permission(UserServiceConstants.SERVICE_NAME, "update", List.of(READ));
	public static final Permission DELETE = new Permission(UserServiceConstants.SERVICE_NAME, "delete", List.of(READ));
	public static final Permission CREATE = new Permission(UserServiceConstants.SERVICE_NAME, "create", List.of(UPDATE, DELETE));
	
	public static final List<Permission> ALL = Collections.unmodifiableList(List.of(READ, UPDATE, DELETE, CREATE));
	
}
