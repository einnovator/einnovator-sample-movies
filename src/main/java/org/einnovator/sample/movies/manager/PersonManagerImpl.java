/**
 * 
 */
package org.einnovator.sample.movies.manager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.jpa.manager.ManagerBaseImpl3;
import org.einnovator.jpa.repository.RepositoryBase2;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.PersonFilter;
import org.einnovator.sample.movies.repository.PersonRepository;
import org.einnovator.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author jsima
 *
 */
@Service
public class PersonManagerImpl extends ManagerBaseImpl3<Person> implements PersonManager {

	private final Log logger = LogFactory.getLog(getClass());

	public static final String PERSONS_RESOURCE_JSON = "data/persons.json";

	@Autowired
	private PersonRepository repository;
	
	@Override
	protected RepositoryBase2<Person, Long> getRepository() {
		return repository;
	}
	
	@Override
	public Person findOneByNameAndSurname(String name, String surname) {
		Optional<Person> person = repository.findOneByNameAndSurname(name, surname);
		return person.isPresent() ? processAfterLoad(person.get(), null) : null;
	}

	@Override
	public Page<Person> findAll(PersonFilter filter, Pageable pageable) {
		populate();
		Page<Person> page = null;
		if (filter!=null) {
			if (StringUtils.hasText(filter.getQ())) {
				String q = filter.getQ().trim();
				page = repository.findAllByNameLikeOrSurnameLike(q, q, pageable);
			} else if (filter.getRole()!=null) {
				page = repository.findAllByRole(filter.getRole(), pageable);
			}
		}
		if (page==null) {
			page = repository.findAll(pageable);
		}
		return processAfterLoad(page, null);
	}

	private boolean init;

	public void populate() {
		populate(false);
	}
	
	@Override
	public void populate(boolean force) {
		if (force || !init) {
			init = true;
			if (!force && repository.count()!=0) {
				return;
			}
			logger.info("populate: ");
			Person[] persons = MappingUtils.readJson(new ClassPathResource(PERSONS_RESOURCE_JSON), Person[].class);
			createOrUpdate(Arrays.asList(persons));
		}
		
	}

	@Override
	public void createOrUpdate(List<Person> persons) {
		if (persons != null) {
			for (Person person : persons) {
				if (person.getName()==null || person.getSurname()==null) {
					logger.warn("createOrUpdate: skipping:" + person);
					continue;
				}
				Person person2 = findOneByNameAndSurname(person.getName(), person.getSurname());
				if (person2 != null) {
					person.setId(person2.getId());
					logger.info("createOrUpdate: update:" + person);
					update(person);
				} else {
					logger.info("createOrUpdate: create:" + person);
					create(person);
				}
			}
		}
	}


}
