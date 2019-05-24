package org.einnovator.sample.movies.manager;

import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.modelx.MovieFilter;

import java.util.List;

import org.einnovator.jpa.manager.ManagerBase2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieManager extends ManagerBase2<Movie, Long>{

	Page<Movie> findAll(MovieFilter filter, Pageable pageable);

	Movie findOneByTitle(String title);

	Person findPerson(Movie movie, String id);
	Person addPerson(Movie movie, Person person, boolean publish);	
	Person removePerson(Movie movie, Person person, boolean publish);
	Person updatePerson(Movie movie, Person person, boolean publish);
	
	void createOrUpdateByTitle(List<Movie> movies);

	void populate(boolean force);

	
}
