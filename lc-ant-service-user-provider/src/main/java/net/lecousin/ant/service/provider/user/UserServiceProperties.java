package net.lecousin.ant.service.provider.user;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties("lc-ant.service.user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceProperties {

	private Duration userTokenDuration = Duration.ofMinutes(15);
	private Duration userRenewTokenDuration = Duration.ofHours(2);
	
}
