package org.einnovator.sample.movies.config;

import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("movies")
public class MoviesConfiguration extends ObjectBase {

	public MoviesConfiguration() {
	}
	


	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator;
	}

}
