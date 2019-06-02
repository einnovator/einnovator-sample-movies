package org.einnovator.sample.movies.manager;

import org.einnovator.sample.movies.model.PersonInMovie;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.modelx.MovieFilter;

import java.util.List;

import org.einnovator.jpa.manager.ManagerBase2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieManager extends ManagerBase2<Movie, Long>{

	Page<Movie> findAll(MovieFilter filter, Pageable pageable);

	Movie findOneByTitle(String title);

	PersonInMovie findPerson(Movie movie, String id);
	PersonInMovie addPerson(Movie movie, PersonInMovie person, boolean publish);	
	PersonInMovie removePerson(Movie movie, PersonInMovie person, boolean publish);
	PersonInMovie updatePerson(Movie movie, PersonInMovie person, boolean publish);
	
	void createOrUpdateByTitle(List<Movie> movies);

	void populate(boolean force);

	
}
