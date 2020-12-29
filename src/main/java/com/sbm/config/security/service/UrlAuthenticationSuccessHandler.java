package com.sbm.config.security.service;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sbm.common.security.SecurityUtils;
import com.sbm.modules.consent.model.User;
import com.sbm.modules.consent.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {

	private static final Logger LOG = LoggerFactory.getLogger(UrlAuthenticationSuccessHandler.class);
	public static final String REDIRECT_PATH = "/secure/two_factor_authentication";

	@Autowired
	private UserService userService;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		handle(request, response, authentication);
		
	}
	
	protected void handle(
	        HttpServletRequest request,
	        HttpServletResponse response, 
	        Authentication authentication
	) throws IOException {

		setLoggedInUserInContext(authentication);

		UserDetails user = (UserDetails)authentication.getPrincipal();
		StringBuilder location = new StringBuilder();
		response.setStatus(302);
		String currentUrl = request.getHeader("Referer");
		HttpSession userSession = request.getSession();
		if(currentUrl != null && currentUrl.contains("original-url")) {
			String externalUrl = currentUrl.split("original-url=")[1];
			location.append(externalUrl)
					.append("&username=")
					.append(user.getUsername())
					.append("&confirmation=")
					.append("AB");

			userSession.setAttribute("confirmation", "AB");
			System.out.println("currentUrl " + currentUrl);
			LOG.info("currentUrl " + currentUrl);
			System.out.println("Redirect location original = " + location.toString());
			LOG.info("Redirect location original = " + location.toString());
			redirectStrategy.sendRedirect(request, response, URLDecoder.decode( location.toString(), "UTF-8" ));
		} else {
			if(userSession != null && userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null && !"".equals(userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST"))) {
				SavedRequest previousSavedUrl = (SavedRequest)userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
				userSession.setAttribute("savedUrl",previousSavedUrl.getRedirectUrl());
				System.out.println("without original url ===== == == " + previousSavedUrl.getRedirectUrl());
				LOG.info("without original url ===== == == " + previousSavedUrl.getRedirectUrl());
				redirectStrategy.sendRedirect(request, response, REDIRECT_PATH);
			}
		}
	    
	}

	private void setLoggedInUserInContext(Authentication authentication) {
		try {
			User user = userService.findUserByUsername(authentication.getName());
			if (user != null) {
				SecurityUtils.configureSecurityContextForAuthenticatedUser(user);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isAuthenticated(){
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}

}
