package net.lecousin.ant.service.user.dto;

import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.lecousin.ant.core.springboot.security.JwtResponse;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AuthenticatedUserResponse extends JwtResponse {

	private User user;
	
	public AuthenticatedUserResponse(
		String accessToken,
		Instant accessTokenExpiresAt,
		String refreshToken,
		Instant refreshTokenExpiresAt,
		User user
	) {
		super(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
		this.user = user;
	}
	
	public AuthenticatedUserResponse(JwtResponse jwt, User user) {
		this(jwt.getAccessToken(), jwt.getAccessTokenExpiresAt(), jwt.getRefreshToken(), jwt.getRefreshTokenExpiresAt(), user);
	}
	
}
