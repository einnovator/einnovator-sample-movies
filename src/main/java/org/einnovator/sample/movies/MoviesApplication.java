package org.einnovator.sample.movies;

import org.einnovator.sample.movies.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MoviesApplication {

	public static void main(String[] args) {
		//new SpringApplicationBuilder(MoviesApplication.class).profiles(AppConfig.getProfiles()).build(args);
		SpringApplication.run(MoviesApplication.class, args);
	}
	

}
