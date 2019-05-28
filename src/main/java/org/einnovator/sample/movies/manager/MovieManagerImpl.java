package org.einnovator.sample.movies.manager;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.jpa.manager.ManagerBaseImpl3;
import org.einnovator.sample.movies.config.MoviesConfiguration;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.MovieFilter;
import org.einnovator.sample.movies.repository.MovieRepository;
import org.einnovator.sample.movies.repository.PersonRepository;
import org.einnovator.social.client.manager.ChannelManager;
import org.einnovator.social.client.model.Channel;
import org.einnovator.sso.client.manager.UserManager;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.UriUtils;
import org.einnovator.util.model.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Transactional
public class MovieManagerImpl extends ManagerBaseImpl3<Movie> implements MovieManager {

	public static final String MOVIES_RESOURCE_JSON = "data/movies.json";
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MovieRepository repository;

	@SuppressWarnings("unused")
	@Autowired
	private PersonManager personManager;

	@SuppressWarnings("unused")
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private ChannelManager channelManager;
	
	@SuppressWarnings("unused")
	@Autowired
	private MoviesConfiguration config;

	@Autowired
	private UIConfiguration ui;

	@Autowired
	private AppConfiguration app;

	public MovieManagerImpl() {

	}
	
	
	@Override
	protected MovieRepository getRepository() {
		return repository;
	}

	@Override
	public Movie findOneByTitle(String title) {
		Optional<Movie> movie = repository.findOneByTitle(title);
		return movie.isPresent() ? processAfterLoad(movie.get(), null) : null;
	}

	@Override
	public Page<Movie> findAll(MovieFilter filter, Pageable pageable) {
		populate();
		Page<Movie> page = null;
		if (filter!=null) {
			if (StringUtils.hasText(filter.getQ()) && filter.getGenre()!=null) {
				page = repository.findAllByTitleLikeAndGenre("%"+filter.getQ().trim()+"%".toLowerCase(), filter.getGenre(), pageable);
			} else if (StringUtils.hasText(filter.getQ())) {
				page = repository.findAllByTitleLike("%"+filter.getQ().trim()+"%", pageable);
			} else if (filter.getGenre()!=null) {
				page = repository.findAllByGenre(filter.getGenre(), pageable);
			}
		}
		if (page==null) {
			page = repository.findAll(pageable);
		}
		return processAfterLoad(page, null);
	}

	@Override
	public Person findPerson(Movie movie, String id) {
		Movie movie2 = movie.getId()!=null ? findById(movie.getId()) : findByUuid(movie.getUuid());
		if (movie2==null) {
			logger.error("addPerson: not found: " + movie);
			return null;
		}
		return movie2.findPerson(id);
	}


	@Override
	public Person addPerson(Movie movie, Person person, boolean publish) {
		Movie movie2 = movie.getId()!=null ? findById(movie.getId()) : findByUuid(movie.getUuid());
		if (movie2==null) {
			logger.error("addPerson: not found: " + movie);
			return null;
		}
		movie2.addPerson(person);
		processBeforePersistence(movie2);
		if (publish) {
			publishEventUpdate(movie2);
		}
		return person;
	}

	@Override
	public Person removePerson(Movie movie, Person person, boolean publish) {
		Movie movie2 = movie.getId()!=null ? findById(movie.getId()) : findByUuid(movie.getUuid());
		if (movie2==null) {
			logger.error("addPerson: not found: " + movie);
			return null;
		}
		Person person2 = movie2.removePerson(person);
		if (person2==null) {
			logger.error("addPerson: not found: " + person);
			return null;
		}
		processBeforePersistence(movie2);
		if (getRepository().save(movie2)==null) {
			logger.error("addPerson: error: " + movie2);
			return null;
		}
		if (publish) {
			publishEventUpdate(movie2);
		}
		return person;
	}

	@Override
	public Person updatePerson(Movie movie, Person person, boolean publish) {
		Movie movie2 = movie.getId()!=null ? findById(movie.getId()) : findByUuid(movie.getUuid());
		if (movie2==null) {
			logger.error("addPerson: not found: " + movie);
			return null;
		}
		Person person2 = movie2.findPerson(person);
		if (person2==null) {
			logger.error("addPerson: not found: " + person);
			return null;
		}
		MappingUtils.updateObjectFromNonNull(person2, person);
		processBeforePersistence(movie2);
		if (personRepository.save(person2)== null) {
			logger.error("addPerson: failed update: " + movie2);
			return null;
		}
		if (publish) {
			publishEventUpdate(movie2);
		}
		return person2;
	}

	


	
	@Override
	public void processBeforePersistence(Movie movie) {
		super.processBeforePersistence(movie);
		if (movie!=null) {
		}
	}
	
	@Override
	public void processBeforeSave(Movie movie) {
		super.processBeforeSave(movie);
	}
	
	@Override
	public Movie processAfterLoad(Movie movie, Options<Movie> options) {
		super.processAfterLoad(movie, options);
		if (movie!=null) {
		}
		return movie;
	}
	

	@Override
	public void processAfterPersistence(Movie movie) {
		super.processAfterPersistence(movie);
		/*
		if (movie.getChannelId() == null) {
			Channel channel = movie.makeChannel(getBaseUri());
			URI uri = channelManager.createChannel(channel);
			logger.info("processAfterPersistence: createChannel:" + uri + " " + channel);
			if (uri != null) {
				movie.setChannelId(UriUtils.extractId(uri));
				repository.save(movie);
			}
		} else {
			Channel channel = movie.makeChannel(getBaseUri());
			channel.setUuid(movie.getChannelId());
			logger.info("processAfterPersistence: updateChannel:" +channel);
			channelManager.updateChannel(channel);
		}*/
	}
	
	public String getBaseUri() {
		return ui.getLink(app.getId());
	}



	@Transactional
	@Override
	public Movie create(Movie obj) {
		return super.create(obj);
	}

	@Transactional
	@Override
	public Movie update(Movie obj) {
		return super.update(obj);
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
			Movie[] movies = MappingUtils.readJson(new ClassPathResource(MOVIES_RESOURCE_JSON), Movie[].class);
			createOrUpdateByTitle(Arrays.asList(movies));
		}
		
	}

	@Override
	public void createOrUpdateByTitle(List<Movie> movies) {
		if (movies != null) {
			for (Movie movie : movies) {
				if (movie.getTitle()==null) {
					logger.warn("createOrUpdateByTitle: skipping:" + movie);
					continue;
				}
				Movie movie2 = findOneByTitle(movie.getTitle());
				if (movie2 != null) {
					movie.setId(movie2.getId());
					logger.info("createOrUpdateByTitle: update:" + movie);
					update(movie);
				} else {
					logger.info("createOrUpdateByTitle: create:" + movie);
					create(movie);
				}
			}
		}
	}



}
