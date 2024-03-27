package net.lecousin.ant.service.provider.user;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.lecousin.ant.core.security.PermissionDeclaration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserServicePermissions {

	public static final PermissionDeclaration READ = new PermissionDeclaration(UserServiceImpl.SERVICE_NAME, "read", List.of());
	public static final PermissionDeclaration UPDATE = new PermissionDeclaration(UserServiceImpl.SERVICE_NAME, "update", List.of(READ));
	public static final PermissionDeclaration DELETE = new PermissionDeclaration(UserServiceImpl.SERVICE_NAME, "delete", List.of(READ));
	public static final PermissionDeclaration CREATE = new PermissionDeclaration(UserServiceImpl.SERVICE_NAME, "create", List.of(UPDATE, DELETE));
	
	public static final List<PermissionDeclaration> ALL = Collections.unmodifiableList(List.of(READ, UPDATE, DELETE, CREATE));
	
}
