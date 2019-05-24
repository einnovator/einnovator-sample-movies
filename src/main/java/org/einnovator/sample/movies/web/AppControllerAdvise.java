package org.einnovator.sample.movies.web;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.common.web.CommonControllerAdvise;
import org.einnovator.notifications.client.manager.NotificationManager;
import org.einnovator.notifications.client.manager.PreferencesManager;
import org.einnovator.sso.client.SsoClient;
import org.einnovator.sso.client.manager.RoleManager;
import org.einnovator.sso.client.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice(assignableTypes = {HomeController.class, MovieController.class, PersonController.class})
@Component
public class AppControllerAdvise extends CommonControllerAdvise {

	private final Log logger = LogFactory.getLog(getClass());


	@Autowired
	private SsoClient ssoClient;

	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private PreferencesManager preferencesManager;
	
	@Autowired
	private RoleManager roleManager;

	@Autowired
	private UserManager userManager;

	@Override
	protected Object makePrincipalInfo(Principal principal, boolean invalid) {
		return userManager.getUser(principal.getName());
	}

	@ModelAttribute("notificationCount")
	public Long notificationCount(Principal principal) {
		if (principal == null) {
			return null;
		}
		ssoClient.setupToken();
		return notificationManager.countNotifications(null);
	}

	@ModelAttribute("admin")
	public Boolean admin(Principal principal, HttpSession session) {
		return roleManager.isAdmin(principal);
	}

	@ModelAttribute("pref")
	public Map<String, String> pref(HttpSession session) {
		return preferencesManager.getAll(session);
	}
}
