package net.lecousin.ant.service.provider.user.exceptions;

import net.lecousin.ant.core.api.exceptions.ForbiddenException;
import net.lecousin.commons.io.text.i18n.TranslatedString;

public class RoleRootReservedException extends ForbiddenException {

	private static final long serialVersionUID = 1L;

	public RoleRootReservedException() {
		super(new TranslatedString("service-user", "role root is reserved"));
	}
	
}
