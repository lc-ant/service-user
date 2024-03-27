package net.lecousin.ant.service.provider.user.db;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lecousin.ant.connector.database.annotations.Entity;
import net.lecousin.ant.connector.database.annotations.GeneratedValue;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;

@Entity(domain = "user", name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends MultiTenantEntity {

	@Id
	@GeneratedValue
	private String id;
	
	private String username;
	private String email;
	private boolean emailVerified = false;
	
	private String firstName;
	private String lastName;
	
	private List<String> roles = new LinkedList<>();
	
}
