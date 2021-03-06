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
import org.einnovator.util.security.SecurityUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/person")
public class PersonController extends ControllerBase {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private PersonManager manager;

	@GetMapping
	public String list(@ModelAttribute("filter") PersonFilter filter, PageOptions options,  @RequestParam(required=false) Boolean async,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		Page<Person> page = manager.findAll(filter, options.toPageRequest());
		model.addAttribute("persons", page);
		model.addAttribute("page", page);
		model.addAttribute("pageJson", PageUtil.toJson(page, false));

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("list: %s %s %s", PageUtil.toString(page), filter, options));			
		}
		return Boolean.TRUE.equals(async) ? "person/person-table" : "person/list";

	}

	@GetMapping("/{id:.*}")
	public String show(@PathVariable("id") String id, String slug,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		Person person = manager.find(id);

		if (person == null) {
			flashNotfound("show", request, redirectAttributes);
			return redirect("/person");
		}
		if (!isAllowedView(principal, person)) {
			flashForbidden("show", request, redirectAttributes);
			return redirect("/person");
		}
		model.addAttribute("person", person);
		model.addAttribute("personJson", MappingUtils.toJson(person));

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("show: %s", person));			
		}
		return "person/show";
	}

	@GetMapping("/create")
	public String createGET(@ModelAttribute("person") Person person,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		
		logger.info("create: " + person);
		return "person/edit";
	}

	@PostMapping
	public String createPOST(@ModelAttribute("person") Person person, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		if (!isAllowedCreate(principal, person)) {
			flashForbidden("createPOST", request, redirectAttributes);
			return redirect("/person");
		}
		if (errors.hasErrors()) {
			logger.error("createPOST:  " + HttpStatus.BAD_REQUEST.getReasonPhrase() + ":" + errors);
			return "";
		}
		person.setCreatedBy(SecurityUtil.getPrincipalName());
		Person person2 = manager.create(person, true);
		if (person2 == null) {
			logger.error("createPOST: " + person);
			error(Messages.KEY_CREATE_FAILURE, (Object[])null, Messages.MSG_CREATE_FAILURE, request, redirectAttributes);
			return "";
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("createPOST: %s", person2));			
		}
		model.addAttribute("person", person2);
		return redirect("/person/" + person2.getUuid());
	}

	@GetMapping("/{id:.*}/edit")
	public String editGet(@PathVariable("id") String id,
			Model model, Principal principal, HttpServletRequest request,  RedirectAttributes redirectAttributes) {

		Person person = manager.find(id);
		if (person == null) {
			flashNotfound("editGet", request, redirectAttributes);
			return redirect("/person");
		}
		if (!isAllowedEdit(principal, person)) {
			flashForbidden("show", request, redirectAttributes);
			return redirect("/person");
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("editGet: %s", person));			
		}
		model.addAttribute("person", person);
		return "person/edit";
	}

	@PutMapping("/{id_:.*}")
	public String editPut(@PathVariable("id_") String id_, @ModelAttribute("person") Person person, BindingResult errors,
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {

		Person person0 = manager.find(id_);
		if (person0 == null) {
			flashNotfound("editPut", request, redirectAttributes);
			return redirect("/person");
		}
		if (!isAllowedEdit(principal, person0)) {
			flashForbidden("editPut", request, redirectAttributes);
			return redirect("/person");
		}
		person.setId(person0.getId());
		Person person2 = manager.update(person, true, true);
		if (person2 == null) {
			logger.error("editPut:  " + HttpStatus.BAD_REQUEST.getReasonPhrase());
			return redirect("/person");
		}
		flashSuccess("editPut", request, redirectAttributes);
		model.addAttribute("person", person2);
		return redirect("/person/" + person.getUuid());
	}

	@DeleteMapping("/{id:.*}")
	public String delete(@PathVariable String id, 
			Model model, Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Person person = manager.findByUuid(id);
		if (person == null) {
			flashNotfound("delete", request, redirectAttributes);
			return redirect("/");
		}
		if (!isAllowedDelete(principal, person)) {
			flashForbidden("delete", request, redirectAttributes);
			return redirect("/");
		}

		Person person2 = manager.delete(person, false);
		if (person2 == null) {
			logger.error("delete:" + HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " + id);
			return redirect("/person/" + person.getUuid());
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("delete: %s", person));			
		}
		redirectAttributes.addFlashAttribute(Messages.ATTRIBUTE_INFO, Messages.MSG_DELETE_SUCCESS);
		return redirect("/person");
	}


	protected void addCommonToModel(Principal principal, Model model) {
	}

}
