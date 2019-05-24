package org.einnovator.sample.movies.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends ControllerBase {
	
	private final Log logger = LogFactory.getLog(getClass());

	
	@GetMapping("/")
	public String home(Model model, Principal principal, HttpServletRequest request) {
		logger.info("home:" + principal);
		return redirect("/movie");
	}

	@GetMapping("/admin")
	public String admin(Principal principal, Model model) {
		if (!isAdmin(principal)) {
			return redirect("/");			
		}
		
		return redirect("/admin/movie");
    }


}
