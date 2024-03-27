package net.lecousin.ant.service.provider.user.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lecousin.ant.connector.database.annotations.Entity;
import net.lecousin.ant.connector.database.annotations.GeneratedValue;
import net.lecousin.ant.connector.database.annotations.Index;
import net.lecousin.ant.connector.database.model.IndexType;
import net.lecousin.ant.core.api.ApiData;
import net.lecousin.ant.core.expression.impl.StringFieldReference;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;

@Entity(domain = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@Index(fields = { "tenantId", "username" }, type = IndexType.UNIQUE)
@Index(fields = { "username", "email", "firstName", "lastName" }, type = IndexType.TEXT)
public class UserEntity extends MultiTenantEntity {
	
	public static final StringFieldReference FIELD_ID = ApiData.FIELD_ID;
	public static final StringFieldReference FIELD_USERNAME = new StringFieldReference("username");
	public static final StringFieldReference FIELD_EMAIL = new StringFieldReference("email");
	public static final StringFieldReference.Nullable FIELD_PASSWORD = new StringFieldReference.Nullable("password");

	@Id
	@GeneratedValue
	private String id;
	
	private String username;
	private String email;
	private boolean emailVerified = false;
	
	private String firstName;
	private String lastName;
	
	private List<String> roles = new LinkedList<>();
	
	@JsonIgnore
	// optional because the tenant may use an external authentication service
	private Optional<String> password;
	
}
