package org.einnovator.sample.movies.config;

import org.einnovator.clipboard.ClipboardConfig;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.common.web.InfrastructureConfig;
import org.einnovator.documents.client.config.DocumentsClientConfig;
import org.einnovator.notifications.client.config.NotificationsClientConfig;
import org.einnovator.social.client.config.SocialClientConfig;
import org.einnovator.sso.client.config.SsoClientSecurityConfigurer;
import org.einnovator.vsite.config.SitesConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties({MoviesConfiguration.class, UIConfiguration.class, AppConfiguration.class})
@Import({ SsoClientSecurityConfigurer.class, SitesConfig.class, ClipboardConfig.class, InfrastructureConfig.class,
	NotificationsClientConfig.class, DocumentsClientConfig.class, SocialClientConfig.class})
@Configuration
public class AppConfig {

}
