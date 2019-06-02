package org.einnovator.sample.movies.web;

import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.einnovator.sample.movies.manager.PersonManager;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.PersonFilter;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

@RestController
@RequestMapping("/api/person")
public class PersonRestController extends ControllerBase {

	@Autowired
	private PersonManager manager;


	@GetMapping
	public ResponseEntity<Page<Person>> listPersons(PageOptions options, PersonFilter filter, 
			Principal principal, HttpServletResponse response) {
		if (!isAllowed(principal, false)) {
			return forbidden("listPersons", response);
		}
		Page<Person> page = manager.findAll(filter, options.toPageRequest());
		return ok(page, "listPersons", response,  PageUtil.toString(page), filter, options);
	}

	@PostMapping
	public ResponseEntity<Void> createPerson(@RequestBody Person person, BindingResult errors,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		if (!isAllowedCreate(principal, person)) {
			return forbidden("createPerson", response);
		}

		if (errors.hasErrors()) {
			String err = errors.getAllErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(","));
			return badrequest("createPerson", response, err);
		}

		Person person2 = manager.create(person, false);
		if (person2 == null) {
			return badrequest("createPerson", response);
		}
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(person2.getUuid());
		return created(location, "createPerson", response);
	}


	@GetMapping("/{id:.*}")
	public ResponseEntity<Person> getPerson(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Person person = manager.find(id);
		if (person == null) {
			return notfound("getPerson", response);
		}
		if (!isAllowedView(principal, person)) {
			return forbidden("getPerson", response);
		}
		return ok(person, "getPerson", response);
	}

	@PutMapping("/{_id:.*}")
	public ResponseEntity<Void> updatePerson(@PathVariable("_id") String id, @RequestBody Person person,
		Principal principal, HttpServletResponse response) {		
		Person person0 = manager.find(id);
		if (person0 == null) {
			return notfound("updatePerson", response);
		}
		person.setUuid(id);
		if (!isAllowedUpdate(principal, person)) {
			return forbidden("updatePerson", response);
		}
		Person person2 = manager.update(person, true, false);
		if (person2 == null) {
			return badrequest("updatePerson", response, id);
		}
		return nocontent("updatePerson", response);
	}


	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deletePerson(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Person person = manager.find(id);
		if (person == null) {
			return notfound("deletePerson", response);
		}
		if (!isAllowedDelete(principal, person)) {
			return forbidden("deletePerson", response);
		}
		Person person2 = manager.delete(person);
		if (person2 == null) {
			return badrequest("deletePerson", response, id);
		}
		return nocontent("deletePerson", response);
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
		return true; //permissive
	}

}
