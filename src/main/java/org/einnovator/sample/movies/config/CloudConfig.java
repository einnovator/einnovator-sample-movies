package org.einnovator.sample.movies.config;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


@Profile("cloud")
@Configuration
public class CloudConfig {

	@SuppressWarnings("unused")
	@Autowired 
	private MoviesConfiguration config;
	
	@Bean
	public Cloud cloud() {
		CloudFactory factory = new CloudFactory();
		return factory.getCloud();
	}


	@Bean
	@Primary
	public DataSource dataSource(Cloud cloud) {
		//DataSourceConfig dbConfig = new DataSourceConfig(config.getPool().makeCloudPoolConfig(), null);
		return cloud.getServiceConnector("movies-db", DataSource.class, null /*dbConfig*/);
	}

	@Bean
	public ConnectionFactory connectionFactory(Cloud cloud) {
		return cloud.getServiceConnector("amqp", ConnectionFactory.class, null);
	}

}
