package org.einnovator.sample.movies.repository;

import java.util.Optional;

import org.einnovator.jpa.repository.RepositoryBase2;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.model.MovieGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepository extends RepositoryBase2<Movie, Long> {

	Optional<Movie> findOneByTitle(String title);
	
	Page<Movie> findAllByTitleLike(String title, Pageable pageable);
	Page<Movie> findAllByGenre(MovieGenre genre, Pageable pageable);
	Page<Movie> findAllByTitleLikeAndGenre(String title, MovieGenre genre, Pageable pageable);

}

