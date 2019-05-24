package org.einnovator.sample.movies.web;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.sample.movies.config.MoviesConfiguration;
import org.einnovator.sso.client.manager.GroupManager;
import org.einnovator.sso.client.manager.RoleManager;
import org.einnovator.sso.client.manager.UserManager;
import org.einnovator.vsite.manager.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

public abstract class ControllerBase extends org.einnovator.common.web.ControllerBase {
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected MoviesConfiguration config;

	@Autowired
	protected UIConfiguration ui;
	
	@Autowired
	protected RoleManager roleManager;
	
	@Autowired
	protected SiteManager siteManager;

	@Autowired
	protected UserManager userManager;

	@Autowired
	protected GroupManager groupManager;
	
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected LocaleResolver localeResolver;


	
	protected boolean isAllowed(Principal principal, boolean admin) {
		if (principal==null) {
			return false;
		}
		if (admin) {
			return isAdmin(principal);
		}
		return true;
	}
	
	protected boolean isAdmin(Principal principal) {
		return roleManager.isAdmin(principal);
	}

	protected boolean isAllowed(Principal principal, String user) {
		if (principal == null) {
			return false;
		}
		if (principal.getName().equals(user)) {
			return true;
		}
		if (isAdmin(principal)) {
			return true;
		}
		return false;
	}
	

	protected boolean isAllowedView(Principal principal, EntityBase2<?> obj) {
		if (isAllowed(principal, obj)) {
			return true;
		}
		return false;
	}

	protected boolean isAllowedCreate(Principal principal, EntityBase2<?> obj) {
		if (principal==null) {
			return false;
		}
		return true;
	}

	protected boolean isAllowedEdit(Principal principal, EntityBase2<?> obj) {
		if (isAllowed(principal, obj)) {
			return true;
		}
		return false;
	}

	protected boolean isAllowedDelete(Principal principal, EntityBase2<?> obj) {
		if (isAllowed(principal, obj)) {
			return true;
		}
		return false;
	}

	protected boolean isAllowed(Principal principal, EntityBase2<?> obj) {
		return isAllowed(principal, obj.getCreatedBy());
	}
	
	protected boolean isMember(Principal principal, String groupId) {
		return groupManager.isMember(groupId, principal.getName());
	}

	

}
