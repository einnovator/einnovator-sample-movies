package org.einnovator.sample.movies.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.einnovator.documents.client.web.DocumentRestController;
import org.einnovator.sso.client.SsoClient;

@Controller
public class AppRestDocumentController extends DocumentRestController {

	
	@Autowired
	private SsoClient ssoClient;
	
	
	@Override
	protected void setupToken(Principal principal, Authentication authentication) {
		ssoClient.setupToken();
	}
	

}
