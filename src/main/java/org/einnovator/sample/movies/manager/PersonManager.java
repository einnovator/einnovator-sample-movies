/**
 * 
 */
package org.einnovator.sample.movies.manager;

import java.util.List;

import org.einnovator.jpa.manager.ManagerBase2;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.PersonFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author support@einnovator.org
 *
 */
public interface PersonManager extends ManagerBase2<Person, Long> {

	Page<Person> findAll(PersonFilter filter, Pageable pageable);

	Person findOneByNameAndSurname(String name, String surname);

	void createOrUpdate(List<Person> persons);

	void populate(boolean force);

}
