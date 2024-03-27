package net.lecousin.ant.service.provider.user.db;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lecousin.ant.connector.database.annotations.Entity;
import net.lecousin.ant.connector.database.annotations.GeneratedValue;
import net.lecousin.ant.connector.database.annotations.Index;
import net.lecousin.ant.connector.database.model.IndexType;
import net.lecousin.ant.core.api.ApiData;
import net.lecousin.ant.core.expression.impl.StringFieldReference;
import net.lecousin.ant.core.springboot.service.provider.helper.MultiTenantEntity;

@Entity(domain = "user", name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@Index(fields = { "tenantId", "name" }, type = IndexType.UNIQUE)
public class RoleEntity extends MultiTenantEntity {
	
	public static final StringFieldReference FIELD_ID = ApiData.FIELD_ID;
	public static final StringFieldReference FIELD_NAME = new StringFieldReference("name");

	@Id
	@GeneratedValue
	private String id;
	
	private String name;
	
	private List<String> authorities = new LinkedList<>();
	
}
