package net.lecousin.ant.service.provider.user;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.lecousin.ant.connector.database.DatabaseConnectorConfiguration;
import net.lecousin.ant.core.springboot.service.provider.helper.LcAntServiceProviderHelperConfiguration;

@Configuration
@EnableAutoConfiguration
@Import({DatabaseConnectorConfiguration.class, LcAntServiceProviderHelperConfiguration.class})
@ComponentScan
@EnableConfigurationProperties(UserServiceProperties.class)
public class UserServiceConfiguration {

}
