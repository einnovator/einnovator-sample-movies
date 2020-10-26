package org.einnovator.sample.movies.config;

import org.einnovator.clipboard.ClipboardConfig;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.common.web.InfrastructureConfig;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.notifications.client.config.NotificationsClientConfig;
import org.einnovator.social.client.config.SocialClientConfig;
import org.einnovator.sso.client.config.SsoClientSecurityConfigurer;
import org.einnovator.util.EnvUtil;
import org.einnovator.vsite.config.SitesConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@EnableConfigurationProperties({MoviesConfiguration.class, UIConfiguration.class, AppConfiguration.class})
@Import({ SsoClientSecurityConfigurer.class, SitesConfig.class, ClipboardConfig.class, InfrastructureConfig.class,
	NotificationsClientConfig.class, DocumentsClientConfig.class, SocialClientConfig.class})
@Configuration
public class AppConfig {

	@Bean(name="dataSource")
	@Profile("!mysql")
	public EmbeddedDatabase datasourceHsql() {
		 return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
	}
	
	public static String[] getProfiles() {
		String dbUrl = EnvUtil.getEnv("spring.datasource.url");
		if (dbUrl!=null && dbUrl.startsWith("jdbc:mysql")) {
			return new String[] {"mysql"};
		};
		return new String[0];
	}
}
