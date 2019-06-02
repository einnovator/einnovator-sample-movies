package org.einnovator.sample.movies.repository;

import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.model.MovieRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.einnovator.jpa.repository.RepositoryBase2;

public interface PersonRepository extends RepositoryBase2<Person, Long> {

	Page<Person> findAllByNameLikeOrSurnameLike(String q, String q2, Pageable pageable);

	Optional<Person> findOneByNameAndSurname(String name, String surname);

	Optional<Person> findOneByNameOrSurname(String name, String surname);

	//Page<Person> findAllByRole(MovieRole role, Pageable pageable);


}
