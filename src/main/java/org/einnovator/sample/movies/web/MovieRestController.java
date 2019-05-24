package org.einnovator.sample.movies.web;

import java.net.URI;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.sample.movies.manager.MovieManager;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.MovieFilter;
import org.einnovator.util.PageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;



@RestController
@RequestMapping("/api/movie")
public class MovieRestController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MovieManager manager;

	@GetMapping
	public ResponseEntity<Page<Movie>> listMovies(MovieFilter filter, PageOptions options, @RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal==null) {
			logger.error("listMovies: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<Page<Movie>>(HttpStatus.UNAUTHORIZED);				
		}

		if (!isAllowed(principal, false)) {
			logger.error("listMovies: " + format(HttpStatus.FORBIDDEN) + " " + principal.getName());
			return new ResponseEntity<Page<Movie>>(HttpStatus.FORBIDDEN);
		}
		Page<Movie> movies = manager.findAll(filter, options.toPageRequest());
		movies = manager.processBeforeMarshalling(movies, true);
		logger.info("listMovies: " + (movies!=null ? " #" + movies.getTotalElements() : "") + " " + filter + " " + options);
		ResponseEntity<Page<Movie>> result = new ResponseEntity<Page<Movie>>(movies, HttpStatus.OK);
		return result;			
	}
	

	
	@PostMapping
	public ResponseEntity<Void> createMovie(@RequestBody Movie movie, HttpServletRequest request, @RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal==null) {
			logger.error("createMovie: " + format(HttpStatus.UNAUTHORIZED) + " : " + movie);
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);				
		}
		if (!isAllowedCreate(principal, movie)) {
			logger.error("createMovie: " + format(HttpStatus.FORBIDDEN) + " : " + movie);
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		Movie movie2 = manager.create(movie, false);
		if (movie2==null) {
			logger.error("createMovie: " + format(HttpStatus.BAD_REQUEST) + " : " + movie);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		String id = movie2.getUuid();
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(id);
		logger.info("createMovie: " + location + " " + movie);
		ResponseEntity<Void> result = ResponseEntity.created(location).build();
		return result;
	}
	
	@GetMapping("/{id:.*}")
	public ResponseEntity<Movie> getMovie(@PathVariable String id, @RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal==null) {
			logger.error("getMovie: " + format(HttpStatus.UNAUTHORIZED) + " : " + id);
			return new ResponseEntity<Movie>(HttpStatus.UNAUTHORIZED);				
		}
		Movie movie = manager.findByUuid(id);
		if (movie==null) {
			logger.error("getMovie: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedView(principal, movie)) {
			logger.error("getMovie: " + format(HttpStatus.FORBIDDEN) + " : " + id);
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
		logger.info("getMovie: " + movie);
		ResponseEntity<Movie> result = new ResponseEntity<Movie>(movie, HttpStatus.OK);
		return result;
	}
	
	@PutMapping("/{id:.*}")
	public ResponseEntity<Void> updateMovie(@RequestBody Movie movie, @PathVariable String id, @RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal==null) {
			logger.error("updateMovie: " + format(HttpStatus.UNAUTHORIZED) + " : " + movie);
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);				
		}
		Movie movie0 = manager.findByUuid(id);
		if (movie0==null) {
			logger.error("updateMovie: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedEdit(principal, movie)) {
			logger.error("updateMovie: " + format(HttpStatus.FORBIDDEN) + " : " + movie);
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		movie.setUuid(id);
		movie.setId(movie0.getId());
		Movie movie2 = manager.update(movie, true, true);
		if (movie2==null) {
			logger.error("updateMovie:" + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		logger.info("updateMovie: " + movie2);
		ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return result;
	}
	
	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deleteMovie(@PathVariable String id, @RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal==null) {
			logger.error("deleteMovie: " + format(HttpStatus.UNAUTHORIZED) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);				
		}
		Movie movie = manager.findByUuid(id);
		if (movie==null) {
			logger.error("getMovie: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedDelete(principal, movie)) {
			logger.error("deleteMovie: " + format(HttpStatus.FORBIDDEN) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}

		Movie movie2 = manager.delete(movie, true);
		if (movie2==null) {
			logger.error("deleteMovie:" + format(HttpStatus.BAD_REQUEST) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		logger.info("deleteMovie: " + movie);
		return result;
	}
	
	//
	// Persons
	//

	@PostMapping("/{pid:.*}/person")
	public ResponseEntity<Person> createPersonPOST(@PathVariable("pid") String pid, @RequestBody @Valid Person person, BindingResult errors,
			@RequestParam(required=false) Boolean publish, Principal principal, HttpServletRequest request) {
		if (principal == null) {
			logger.error("createPersonPOST: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<Person>(HttpStatus.UNAUTHORIZED);
		}
		Movie movie = manager.find(pid);
		if (movie == null) {
			logger.error("createPersonPOST: " + HttpStatus.NOT_FOUND.getReasonPhrase());
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedEdit(principal, movie)) {
			logger.error("createPersonPOST: " + HttpStatus.FORBIDDEN.getReasonPhrase());
			return new ResponseEntity<Person>(HttpStatus.FORBIDDEN);
		}
		if (errors.hasErrors()) {
			logger.error("createPersonPOST: " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}
		Person person2 = manager.addPerson(movie, person, Boolean.TRUE.equals(publish));
		if (person2 == null) {
			logger.error("createPersonPOST: " + person);
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}
		logger.info("createPOST: " + person);

		String personId = person2.getUuid();
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(personId);
		logger.info("createPersonPOST: " + location + " " + person2);
		ResponseEntity<Person> result = new ResponseEntity<Person>(person2, HttpStatus.OK);
		return result;
	}

	@DeleteMapping("/{pid:.*}/person/{id:.*}")
	public ResponseEntity<Person> deletePerson(@PathVariable("pid") String pid, @PathVariable("id") String id,
			@RequestParam(required=false) Boolean publish, Principal principal) {
		if (principal == null) {
			logger.error("deletePerson: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<Person>(HttpStatus.UNAUTHORIZED);
		}
		Movie movie = manager.find(pid);
		if (movie == null) {
			logger.error("createPersonPOST: " + HttpStatus.NOT_FOUND.getReasonPhrase());
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedEdit(principal, movie)) {
			logger.error("createPersonPOST: " + HttpStatus.FORBIDDEN.getReasonPhrase());
			return new ResponseEntity<Person>(HttpStatus.FORBIDDEN);
		}
		Person person = manager.findPerson(movie, id);
		if (person == null) {
			logger.error("deletePerson: " + HttpStatus.NOT_FOUND.getReasonPhrase());
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}
		Person person2 = manager.removePerson(movie, person, Boolean.TRUE.equals(publish));
		if (person2 == null) {
			logger.error("deletePerson:" + format(HttpStatus.BAD_REQUEST) + " : " + person2);
			return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
		}
		ResponseEntity<Person> result = new ResponseEntity<Person>(person2, HttpStatus.OK);
		logger.info("remove: " + person2);
		return result;
	}



	private boolean isAllowedView(Principal principal, Movie movie) {
		if (isAllowed(principal, movie)) {
			return true;
		}
		return false;
	}

	private boolean isAllowedCreate(Principal principal, Movie movie) {
		if (isAllowed(principal, movie)) {
			return true;
		}
		return false;
	}

	private boolean isAllowedEdit(Principal principal, Movie movie) {
		if (isAllowed(principal, movie)) {
			return true;
		}
		return false;
	}

	private boolean isAllowedDelete(Principal principal, Movie movie) {
		if (isAllowed(principal, movie)) {
			return true;
		}
		return false;
	}

	private boolean isAllowed(Principal principal, Movie movie) {
		return isAllowed(principal, movie.getCreatedBy());
	}
	
}
