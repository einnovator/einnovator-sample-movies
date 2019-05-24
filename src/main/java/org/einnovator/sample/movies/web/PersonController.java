package org.einnovator.sample.movies.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.sample.movies.manager.PersonManager;
import org.einnovator.sample.movies.model.Person;
import org.einnovator.sample.movies.modelx.PersonFilter;
import org.einnovator.util.MappingUtils;
import org.einnovator.util.PageOptions;
import org.einnovator.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping({"/person", "/admin/person"})
public class PersonController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private PersonManager manager;

	@GetMapping
	public String list(@ModelAttribute("filter") PersonFilter filter, PageOptions options, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("list", request, redirectAttributes);
			return redirect("/");
		}
		
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Page<Person> page = manager.findAll(filter, options.toPageRequest());
		model.addAttribute("persons", page);
		model.addAttribute("page", page);

		logger.info("list: " + PageUtil.toString(page) + " " + filter + " " + options);
		return "person/list-persons";
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

		Person person = manager.find(id);

		if (person == null) {
			notfound("show", request, redirectAttributes);
			return redirect("/person");
		}
		if (!isAllowedView(principal, person)) {
			forbidden("show", request, redirectAttributes);
			return redirect("/person");
		}
		model.addAttribute("person", person);
		model.addAttribute("personJson", MappingUtils.toJson(person));


		logger.info("show: " + person);
		return "person/" + (_admin ? "show" : "view");
	}

	@GetMapping("/create")
	public String createGET(String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("show", request, redirectAttributes);
			return "";
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return "";
		}
		Person person = new Person();
		logger.info("create: " + person);
		model.addAttribute("person", person);
		return "person/person-details";
	}

	@PostMapping
	public String createPOST(@RequestBody Person person, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("createPOST", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		if (!isAllowedCreate(principal, person)) {
			forbidden("createPOST", request, redirectAttributes);
			return redirect("/person");
		}
		if (errors.hasErrors()) {
			logger.error("createPOST:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return "";
		}
		person.setCreatedBy(principal.getName());
		Person person2 = manager.create(person, true);
		if (person2 == null) {
			logger.error("createPOST: " + person);
			error(Messages.KEY_CREATE_FAILURE, null, Messages.MSG_CREATE_FAILURE, request, redirectAttributes);
			return "";
		}
		logger.info("createPOST: " + person2);
		model.addAttribute("person", person2);
		return "person/person-details";
	}

	@GetMapping("/{id:.*}/edit")
	public String editGet(@PathVariable("id") String id, String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		if (principal == null) {
			unauthorized("show", request, redirectAttributes);
			return redirect("/");
		}
		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}
		Person person = new Person();
		Person person0 = manager.find(id);
		if (person0 != null) {
			person = person0;
			if (!isAllowedView(principal, person)) {
				forbidden("show", request, redirectAttributes);
			}
		}
		
		logger.info("editGet: " + person);
		model.addAttribute("person", person);
		return "person/person-details";
	}

	@PutMapping("/{id_:.*}")
	public String editPut(@PathVariable("id_") String id_, Person person, BindingResult errors,
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

		Person person0 = manager.find(id_);
		if (person0 == null) {
			notfound("editPut", request, redirectAttributes);
			return "";
		}
		if (!isAllowedEdit(principal, person0)) {
			forbidden("editPut", request, redirectAttributes);
			return redirect("/person");
		}
		person.setId(person0.getId());
		Person person2 = manager.update(person, true, true);
		if (person2 == null) {
			logger.error("editPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
			return redirect("/person");
		}
		success(request, redirectAttributes);
		logger.info("editPut: " + person2);
		model.addAttribute("person", person2);
		return "person/person-details";
	}

	@DeleteMapping("/{id:.*}")
	public String delete(@PathVariable String id, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (principal == null) {
			unauthorized("delete", request, redirectAttributes);
			return redirect("/");
		}

		
		boolean _admin = adminAccess(request, model);
		if (_admin && !isAdmin(principal)) {
			return redirect("/");
		}

		Person person = manager.findByUuid(id);
		if (person == null) {
			notfound("delete", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedDelete(principal, person)) {
			forbidden("delete", request, redirectAttributes);
			return redirect("/");
		}

		Person person2 = manager.delete(person, false);
		if (person2 == null) {
			logger.error("delete:" + HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " + id);
			return redirect("/person/" + person.getUuid());
		}
		logger.info("delete: " + person);
		redirectAttributes.addFlashAttribute(Messages.ATTRIBUTE_INFO, Messages.MSG_DELETE_SUCCESS);
		return redirect(_admin, "/person");
	}


	protected void addCommonToModel(Principal principal, Model model) {
	}

}
