package net.lecousin.ant.service.provider.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.lecousin.ant.service.user.UserAuthenticationService;

@Service("userServiceProviderUserAuthentication")
@Primary
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

}
