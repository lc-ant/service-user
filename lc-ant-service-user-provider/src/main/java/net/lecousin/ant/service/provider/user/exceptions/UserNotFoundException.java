package net.lecousin.ant.service.provider.user.exceptions;

import net.lecousin.ant.core.api.exceptions.NotFoundException;
import net.lecousin.commons.io.text.i18n.TranslatedString;

public class UserNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String userNameOrEmail) {
		super(new TranslatedString("service-user", "user not found {}", userNameOrEmail), "user");
	}
	
}
