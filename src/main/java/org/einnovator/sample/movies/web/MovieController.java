package org.einnovator.sample.movies.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.sample.movies.manager.MovieManager;
import org.einnovator.sample.movies.model.Movie;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.MovieFilter;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping({"/movie", "/admin/movie"})
public class MovieController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MovieManager manager;

	@GetMapping
	public String list(@ModelAttribute("filter") MovieFilter filter, PageOptions options, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("list", request, redirectAttributes);
			return redirect("/");
		}
		
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}
		
		filter.setRunAs(principal.getName());
		Page<Movie> page = manager.findAll(filter, options.toPageRequest());
		model.addAttribute("movies", page);
		model.addAttribute("page", page);


		logger.info("list: " + PageUtil.toString(page) + " " + filter + " " + options);
		return "movie/list";
	}

	@GetMapping("/{id:.*}")
	public String show(@PathVariable("id") String id, String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("show", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.find(id);

		if (movie == null) {
			notfound("show", request, redirectAttributes);
			return redirect("/movie");
		}
		if (!isAllowedView(principal, movie)) {
			forbidden("show", request, redirectAttributes);
			return redirect("/movie");
		}
		model.addAttribute("movie", movie);
		model.addAttribute("movieJson", MappingUtils.toJson(movie));

		if (!_admin) {
			model.addAttribute("channelId", movie.getChannelId());
		}

		logger.info("show: " + movie);

		model.addAttribute("_edit", isAllowedEdit(principal, movie));
		model.addAttribute("_manage", isAllowedEdit(principal, movie));

		return "movie/show";
	}

	@GetMapping("/create")
	public String createGET(@ModelAttribute("movie") Movie movie, BindingResult errors, @RequestParam(required = false) String type, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("createGET", request, redirectAttributes);
			return redirect("/");
		}

		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		if (!isAllowedCreate(principal, movie)) {
			forbidden("createGET", request, redirectAttributes);
			return redirect("/movie");
		}
		model.addAttribute("type", type);

		addCommonToModel(principal, model);

		return "movie/edit";
	}

	@PostMapping
	public String createPOST(@ModelAttribute("movie") @Valid Movie movie, BindingResult errors, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("createPOST", request, redirectAttributes);
			return redirect("/");
		}

		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		if (!isAllowedCreate(principal, movie)) {
			forbidden("createPOST", request, redirectAttributes);
			return redirect("/movie");
		}
		if (errors.hasErrors()) {
			logger.error("createPOST:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return "movie/edit";
		}
		movie.setCreatedBy(principal.getName());
		Movie movie2 = manager.create(movie, true);
		if (movie2 == null) {
			logger.error("createPOST: " + movie);
			error(Messages.KEY_CREATE_FAILURE, null, Messages.MSG_CREATE_FAILURE, request, redirectAttributes);
			return redirect("/movie");
		}
		logger.info("createPOST: " + movie2);

		return redirect(_admin, "/movie/" + movie2.getUuid());
	}

	@GetMapping("/{id:.*}/edit")
	public String editGet(@PathVariable("id") String id, @RequestParam(required = false) String type, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("editGet", request, redirectAttributes);
			return redirect("/");
		}

		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.find(id);
		if (movie == null) {
			notfound("editGet", request, redirectAttributes);
			return redirect("/movie/" + (type != null ? "? type=" + type : ""));
		}
		if (!isAllowed(principal, movie)) {
			forbidden("editGet", request, redirectAttributes);
			return redirect("/movie");
		}
		model.addAttribute("movie", movie);
		model.addAttribute("movieJson", MappingUtils.toJson(movie));
		
		addCommonToModel(principal, model);

		logger.info("editGet: " + movie);

		return "movie/edit";
	}

	@PutMapping("/{id_:.*}")
	public String editPut(@PathVariable("id_") String id_, @ModelAttribute("movie") @Valid Movie movie, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("editPut", request, redirectAttributes);
			return redirect("/");
		}

		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			forbidden("editPut", request, redirectAttributes);
			return redirect("/");
		}

		Movie movie0 = manager.find(id_);
		if (movie0 == null) {
			notfound("editPut", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedEdit(principal, movie0)) {
			forbidden("editPut", request, redirectAttributes);
			return redirect("/movie");
		}

		if (!StringUtils.hasText(movie0.getCreatedBy())) {
			movie.setCreatedBy(principal.getName());
		}
		movie.setLastModifiedBy(principal.getName());
		movie.setId(movie0.getId());
		Movie movie2 = manager.update(movie, true, true);
		if (movie2 == null) {
			logger.error("editPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
			return redirect("movie");
		}
		info(Messages.KEY_UPDATE_SUCCESS, null, Messages.MSG_UPDATE_SUCCESS, request, redirectAttributes);
		logger.info("editPut: " + movie2);
		return redirect(_admin, "/movie/" + movie2.getUuid());
	}

	@DeleteMapping("/{movieId:.*}")
	public String delete(@PathVariable String movieId, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("delete", request, redirectAttributes);
			return redirect("/");
		}

		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.findByUuid(movieId);
		if (movie == null) {
			notfound("delete", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedDelete(principal, movie)) {
			forbidden("delete", request, redirectAttributes);
			return redirect("/");
		}

		Movie movie2 = manager.delete(movie, false);
		if (movie2 == null) {
			logger.error("delete:" + HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " + movieId);
			return redirect("/movie/" + movie.getUuid());
		}
		logger.info("delete: " + movie);
		redirectAttributes.addFlashAttribute(Messages.ATTRIBUTE_INFO, Messages.MSG_DELETE_SUCCESS);
		return redirect(_admin, "/movie/");
	}


	//
	// Person
	//
	
	
	@GetMapping("/{pid}/person/create")
	public String createPersonGET(@PathVariable("pid") String pid, @ModelAttribute("person") Person person, BindingResult errors,
			Model model, Principal principal, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("createPersonGET", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.find(pid);
		if (movie == null) {
			notfound("createPersonGET", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedEdit(principal, movie)) {
			forbidden("createPersonGET", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}
		model.addAttribute("movie", movie);
		addCommonToModel(principal, model);
		
		return "person/edit";
	}

	
	@PostMapping("/{pid}/person")
	public String createPersonPOST(@PathVariable("pid") String pid, @ModelAttribute("person") @Valid Person person, BindingResult errors, Principal principal,
			Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("createPersonPOST:  ", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.find(pid);
		if (movie == null) {
			notfound("createPersonPOST", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedEdit(principal, movie)) {
			forbidden("createPersonPOST", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}

		if (errors.hasErrors()) {
			model.addAttribute("movie", movie);
			addCommonToModel(principal, model);
			logger.error("createPersonPOST:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return "person/edit";
		}
		Person person2 = manager.addPerson(movie, person, false);
		if (person2 == null) {
			logger.error("createPersonPOST: " + movie);
			error(Messages.KEY_CREATE_FAILURE, null, Messages.MSG_CREATE_FAILURE, request, redirectAttributes);
			return redirect(_admin, "/movie");
		}
		logger.info("createPersonPOST: " + person + " " + movie);
		info(Messages.KEY_CREATE_SUCCESS, null, Messages.MSG_CREATE_SUCCESS, request, redirectAttributes);
		return redirect(_admin, "/movie/" + movie.getUuid());
	}

	@GetMapping("/{pid}/person/{id:.*}/edit")
	public String editPersonGet(@PathVariable String pid, @PathVariable("id") String id, @RequestParam(required = false) String type,
			Model model, HttpServletRequest request, Principal principal, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("editPersonGet", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Movie movie = manager.find(pid);
		if (movie == null) {
			notfound("editPersonGet", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}
		if (!isAllowed(principal, movie)) {
			forbidden("editPersonGet", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}
		model.addAttribute("movie", movie);
		model.addAttribute("courseJson", MappingUtils.toJson(movie));
		
		addCommonToModel(principal, model);

		Person person = movie.findPerson(id);
		if (person==null) {
			notfound("editPersonGet", request, redirectAttributes);
			return redirect(_admin, "/movie/" + movie.getUuid());			
		}
		model.addAttribute("person", person);
		model.addAttribute("personJson", MappingUtils.toJson(person));

		logger.info("editPersonGet: " + movie);

		return "person/edit";
	}

	@PutMapping("/{pid}/person/{id_:.*}")
	public String editPersonPut(@PathVariable String pid, @PathVariable("id_") String id_, @ModelAttribute("person") @Valid Person person, BindingResult errors,
			Principal principal, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("editPut", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}
		
		Movie movie = manager.find(pid);
		if (movie == null) {
			notfound("editPersonPut", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedEdit(principal, movie)) {
			forbidden("editPersonPut", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}

		Person person0 = movie.findPerson(id_);
		if (person0==null) {
			notfound("editPersonPut", request, redirectAttributes);
			return redirect(_admin, "/movie/" + movie.getUuid());			
		}

		person.setId(person0.getId());
		try {
			Person person2 = manager.updatePerson(movie, person, true);
			if (person2 == null) {
				logger.error("editPersonPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
				return redirect(_admin, "/movie/" + movie.getUuid());
			}
			info(Messages.KEY_UPDATE_SUCCESS, null, Messages.MSG_UPDATE_SUCCESS, request, redirectAttributes);
			logger.info("editPersonPut: " + person2);
			
			return redirect(_admin, "/movie/" + movie.getUuid());
		} catch (RuntimeException e) {
			logger.error("editPersonPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + " " + e);
			return redirect(_admin, "/movie/" + movie.getUuid());
		}
	}

	@DeleteMapping("/{pid}/person/{id:.*}")
	public String deletePerson(@PathVariable String pid, @PathVariable String id,
			Model model, Principal principal, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("deletePerson", request, redirectAttributes);
			return redirect("/");
		}

		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}
		
		Movie movie = manager.findByUuid(pid);
		if (movie == null) {
			notfound("deletePerson", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedEdit(principal, movie)) {
			forbidden("deletePerson", request, redirectAttributes);
			return redirect(_admin, "/movie");
		}

		Person person = movie.findPerson(id);
		if (person==null) {
			notfound("deletePerson", request, redirectAttributes);
			return redirect(_admin, "/movie/" + movie.getUuid());			
		}

		try {
			Person person2 = manager.removePerson(movie, person, false);
			if (person2 == null) {
				logger.error("deletePerson:" + HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " + id);
				error(Messages.KEY_DELETE_FAILURE, null, Messages.MSG_DELETE_FAILURE, request, redirectAttributes);
				return redirect(_admin, "/movie/" + movie.getUuid());
			}
			logger.info("deletePerson: " + movie);
			info(Messages.KEY_DELETE_SUCCESS, null, Messages.MSG_DELETE_SUCCESS, request, redirectAttributes);
			return redirect(_admin, "/movie/" + movie.getUuid());			
		} catch (RuntimeException  e) {
			logger.error("deletePerson:" + e + " " + HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " + id);
			error(Messages.KEY_DELETE_FAILURE, null, Messages.MSG_DELETE_FAILURE, request, redirectAttributes);
			return redirect(_admin, "/movie/" + movie.getUuid());			
		}
	}


	//
	// Tags
	//
	
	@Override
	protected boolean isAllowedView(Principal principal, EntityBase2<?> obj) {
		if (isAllowed(principal, false)) {
			return true;
		}
		return true;
	}

	@Override
	protected boolean isAllowedEdit(Principal principal, EntityBase2<?> obj) {
		if (!isAllowedView(principal, obj)) {
			return true;
		}
		return true;
	}

	protected void addCommonToModel(Principal principal, Model model) {
	}
	
}
