package net.lecousin.ant.service.user.dto;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.lecousin.ant.core.api.ApiData;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends ApiData {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private List<String> permissions = new LinkedList<>();
}
