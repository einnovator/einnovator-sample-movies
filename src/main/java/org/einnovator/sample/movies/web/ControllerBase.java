package org.einnovator.sample.movies.web;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.sample.movies.config.MoviesConfiguration;
import org.einnovator.sso.client.manager.GroupManager;
import org.einnovator.sso.client.manager.RoleManager;
import org.einnovator.sso.client.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ControllerBase extends org.einnovator.common.web.ControllerBase {
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	protected MoviesConfiguration config;

	@Autowired
	protected RoleManager roleManager;

	@Autowired
	protected UserManager userManager;

	@Autowired
	protected GroupManager groupManager;
	
	
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
		if (isAllowed(principal, obj.getCreatedBy())) {
			return true;
		}
		return true; //permissive
	}


}
