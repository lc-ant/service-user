package net.lecousin.ant.service.user.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.lecousin.ant.core.api.ApiData;
import net.lecousin.ant.core.validation.ValidationContext;
import net.lecousin.ant.core.validation.annotations.Ignore;
import net.lecousin.ant.core.validation.annotations.Mandatory;
import net.lecousin.ant.core.validation.annotations.StringConstraint;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends ApiData {

	private static final long serialVersionUID = 1L;
	
	@Ignore
	private String tenantId;
	
	@Mandatory(context = ValidationContext.CREATION)
	@Ignore(context = ValidationContext.UPDATE)
	@StringConstraint(minLength = 1, maxLength = 250)
	private String username;
	@Mandatory
	@StringConstraint(minLength = 6, maxLength = 250)
	private String email;
	private boolean emailVerified = false;
	
	private String firstName;
	private String lastName;
	
	private List<String> roles = new LinkedList<>();
	private List<String> authorities = new LinkedList<>();
}
