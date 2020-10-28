package org.einnovator.sample.movies.web;

import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.einnovator.sample.movies.manager.MovieManager;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.modelx.MovieFilter;
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
@RequestMapping("/api/movie")
public class MovieRestController extends ControllerBase {

	@Autowired
	private MovieManager manager;

	@GetMapping
	public ResponseEntity<Page<Movie>> listMovies(PageOptions options, MovieFilter filter, 
			Principal principal, HttpServletResponse response) {
		if (!isAllowed(principal, false)) {
			return forbidden("listMovies", response);
		}
		Page<Movie> page = manager.findAll(filter, options.toPageRequest());
		return ok(page, "listMovies", response,  PageUtil.toString(page), filter, options);
	}

	@PostMapping
	public ResponseEntity<Void> createMovie(@RequestBody Movie movie, BindingResult errors,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		if (!isAllowedCreate(principal, movie)) {
			return forbidden("createMovie", response);
		}

		if (errors.hasErrors()) {
			String err = errors.getAllErrors().stream().map(o -> o.getDefaultMessage()).collect(Collectors.joining(","));
			return badrequest("createMovie", response, err);
		}

		Movie movie2 = manager.create(movie, false);
		if (movie2 == null) {
			return badrequest("createMovie", response);
		}
		URI location = new UriTemplate(request.getRequestURI() + "/{id}").expand(movie2.getUuid());
		return created(location, "createMovie", response);
	}


	@GetMapping("/{id:.*}")
	public ResponseEntity<Movie> getMovie(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Movie movie = manager.find(id);
		if (movie == null) {
			return notfound("getMovie", response);
		}
		if (!isAllowedView(principal, movie)) {
			return forbidden("getMovie", response);
		}
		return ok(movie, "getMovie", response);
	}

	@PutMapping("/{_id:.*}")
	public ResponseEntity<Void> updateMovie(@PathVariable("_id") String id, @RequestBody Movie movie,
		Principal principal, HttpServletResponse response) {		
		Movie movie0 = manager.find(id);
		if (movie0 == null) {
			return notfound("updateMovie", response);
		}
		movie.setUuid(id);
		if (!isAllowedUpdate(principal, movie)) {
			return forbidden("updateMovie", response);
		}
		Movie movie2 = manager.update(movie, true, false);
		if (movie2 == null) {
			return badrequest("updateMovie", response, id);
		}
		return nocontent("updateMovie", response);
	}


	@DeleteMapping("/{id:.*}")
	public ResponseEntity<Void> deleteMovie(@PathVariable String id,
		Principal principal, HttpServletResponse response) {		
		Movie movie = manager.find(id);
		if (movie == null) {
			return notfound("deleteMovie", response);
		}
		if (!isAllowedDelete(principal, movie)) {
			return forbidden("deleteMovie", response);
		}
		Movie movie2 = manager.delete(movie);
		if (movie2 == null) {
			return badrequest("deleteMovie", response, id);
		}
		return nocontent("deleteMovie", response);
	}

	private boolean isAllowedCreate(Principal principal, Movie movie) {
		return true;
	}

	private boolean isAllowedView(Principal principal, Movie movie) {
		return true;
	}

	private boolean isAllowedUpdate(Principal principal, Movie movie) {
		return isAllowed(principal, movie);
	}

	private boolean isAllowedDelete(Principal principal, Movie movie) {
		return isAllowed(principal, movie);
	}

	private boolean isAllowed(Principal principal, Movie movie) {
		if (principal == null) {
			return false;
		}
		if (isAllowed(principal, true)) {
			return true;
		}
		return true; //permissive
	}

	
}
