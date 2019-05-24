package org.einnovator.sample.movies.web;

import java.security.Principal;

import org.einnovator.documents.client.web.FileUploadController;
import org.einnovator.sso.client.SsoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class AppFileUploadController extends FileUploadController {

	
	@Autowired
	private SsoClient ssoClient;
		
	
	@Override
	protected void setupToken(Principal principal, Authentication authentication) {
		ssoClient.setupToken();
	}
	
}
