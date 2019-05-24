package org.einnovator.sample.movies.web;

import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.einnovator.sample.movies.manager.PersonManager;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.PersonFilter;
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
@RequestMapping("/api/person")
public class PersonRestController extends ControllerBase {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private PersonManager manager;


	@GetMapping
	public ResponseEntity<Page<Person>> listPersons(PageOptions options, PersonFilter filter, Principal principal) {
		if (principal == null) {
			logger.error("listPersons: " + format(HttpStatus.UNAUTHORIZED));
			return new ResponseEntity<Page<Person>>(HttpStatus.UNAUTHORIZED);
		}
		if (!isAllowed(principal, false)) {
			logger.error("listPersons: " + format(HttpStatus.FORBIDDEN) + " : " + principal);
			return new ResponseEntity<Page<Person>>(HttpStatus.FORBIDDEN);
		}
		Page<Person> page = manager.findAll(filter, options.toPageRequest());
		logger.info("listPersons: " + (page != null ? " #" + page.getTotalElements() : "") + filter + " " + options);
		ResponseEntity<Page<Person>> result = new ResponseEntity<Page<Person>>(page, HttpStatus.OK);
		return result;
	}

	@PostMapping
	public ResponseEntity<String> createPerson(@RequestBody Person person, HttpServletRequest request, BindingResult errors,
			Principal principal) {
		if (principal == null) {
			logger.error("createPerson: " + format(HttpStatus.UNAUTHORIZED) + " : " + person);
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
		if (!isAllowedCreate(principal, person)) {
			logger.error("createPerson: " + format(HttpStatus.FORBIDDEN) + " : " + person + "  " + principal);
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		}

		if (errors.hasErrors()) {
			String errorMessage = errors.getAllErrors().stream().map(o -> o.getDefaultMessage())
					.collect(Collectors.joining(","));

			logger.error("createPerson: " + format(HttpStatus.BAD_REQUEST) + " " + errorMessage);
			return ResponseEntity.badRequest().body(errorMessage);
		}

		Person person2 = manager.create(person, false);
		if (person2 == null) {
			logger.error("createPerson: " + format(HttpStatus.BAD_REQUEST) + " : " + person);
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		String id = person2.getUuid();
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(id);
		logger.info("createPerson: " + location + " " + person);

		ResponseEntity<String> result = ResponseEntity.created(location).build();
		return result;
	}


	@GetMapping("/{id:.*}")
	public ResponseEntity<Person> getPerson(@PathVariable String id, Principal principal,
			@RequestParam(required = false) Boolean operations) {
		if (principal == null) {
			logger.error("getPerson: " + format(HttpStatus.UNAUTHORIZED) + " : " + id);
			return new ResponseEntity<Person>(HttpStatus.UNAUTHORIZED);
		}
		Person person = manager.find(id);
		if (person == null) {
			logger.error("getPerson: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedView(principal, person)) {
			logger.error("getPerson: " + format(HttpStatus.FORBIDDEN) + " : " + id + "  " + principal);
			return new ResponseEntity<Person>(HttpStatus.FORBIDDEN);
		}
		logger.info("getPerson: " + person);
		ResponseEntity<Person> result = new ResponseEntity<Person>(person, HttpStatus.OK);
		return result;
	}

	@PutMapping("/{id:.*}")
	public ResponseEntity<Void> updatePerson(@RequestBody Person person, @PathVariable String id, Principal principal) {
		if (principal == null) {
			logger.error("updatePerson: " + format(HttpStatus.UNAUTHORIZED) + " : " + person);
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		Person person0 = manager.find(id);
		if (person0 == null) {
			logger.error("getPerson: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		person.setUuid(id);
		if (!isAllowedUpdate(principal, person)) {
			logger.error("updatePerson: " + format(HttpStatus.FORBIDDEN) + " : " + person + "  " + principal);
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		Person person2 = manager.update(person, true, false);
		if (person2 == null) {
			logger.error("updatePerson:" + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		logger.info("updatePerson: " + person2);

		ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return result;
	}

	@PutMapping("admin/{id:.*}")
	public ResponseEntity<Person> updatePersonAdmin(@RequestBody Person person, @PathVariable String id, Principal principal) {
		if (principal == null) {
			logger.error("updatePerson: " + format(HttpStatus.UNAUTHORIZED) + " : " + person);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Person person0 = manager.findByUuid(id);
		if (person0 == null) {
			logger.error("getPerson: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		person.setId(person0.getId());
		if (!isAllowedUpdate(principal, person)) {
			logger.error("updatePerson: " + format(HttpStatus.FORBIDDEN) + " : " + person + "  " + principal);
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		Person person2 = manager.update(person, true, false);
		if (person2 == null) {
			logger.error("updatePerson:" + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		logger.info("updatePerson: " + person2);

		ResponseEntity<Person> result = new ResponseEntity<Person>(person2, HttpStatus.OK);
		return result;
	}

	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deletePerson(@PathVariable String id, Principal principal) {
		if (principal == null) {
			logger.error("deletePerson: " + format(HttpStatus.UNAUTHORIZED) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		Person person = manager.find(id);
		if (person == null) {
			logger.error("deletePerson: " + format(HttpStatus.NOT_FOUND) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		if (!isAllowedDelete(principal, person)) {
			logger.error("deletePerson: " + format(HttpStatus.FORBIDDEN) + ": " + id + "  " + principal);
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		Person person2 = manager.delete(person);
		if (person2 == null) {
			logger.error("deletePerson:" + format(HttpStatus.BAD_REQUEST) + " : " + id);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		ResponseEntity<Void> result = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		logger.info("deletePerson: " + person);
		return result;
	}

	private boolean isAllowedCreate(Principal principal, Person person) {
		return true;
	}

	private boolean isAllowedView(Principal principal, Person person) {
		return true;
	}

	private boolean isAllowedUpdate(Principal principal, Person person) {
		return isAllowed(principal, person);
	}

	private boolean isAllowedDelete(Principal principal, Person person) {
		return isAllowed(principal, person);
	}

	private boolean isAllowed(Principal principal, Person person) {
		if (principal == null) {
			return false;
		}
		if (isAllowed(principal, true)) {
			return true;
		}
		return false;
	}

}
