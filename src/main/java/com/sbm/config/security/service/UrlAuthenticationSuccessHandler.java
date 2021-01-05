package com.sbm.config.security.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

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
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
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
			String serverUrl = getServerUrl(request);
			String authorizeUrl = composeAuthorizeUrl(externalUrl, serverUrl);

			location.append(externalUrl)
					.append("&username=")
					.append(user.getUsername())
					.append("&confirmation=")
					.append("AB");




//			LOG.info("host host host host host host host " + host);

			userSession.setAttribute("confirmation", "AB");
			userSession.setAttribute("userName", user.getUsername());
			System.out.println("currentUrl " + currentUrl);
			userSession.setAttribute("externalUrl", location.toString());
			LOG.info("currentUrl " + currentUrl);
			System.out.println("Redirect location original = " + location.toString());
			LOG.info("Redirect location original = " + location.toString());
//			redirectStrategy.sendRedirect(request, response, URLDecoder.decode( location.toString(), "UTF-8" ));
			userSession.setAttribute("savedUrl",authorizeUrl);
			redirectStrategy.sendRedirect(request, response, authorizeUrl);
//			http://localhost:8080/oauth/authorize?response_type=code&client_id=d6492371-762b-4768-937f-6be6b3cec29f&scope=ReadAccountsBasic+ReadAccountsDetail&redirect_uri=https://www.info-tech.com/app-callback
//			AuthorizationEndpoint
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


	private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	private String getServerUrl(HttpServletRequest request){

		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String host = url.substring(0, url.indexOf(uri));

		return host;
	}

	private String composeAuthorizeUrl(String url, String serverUrl) {
		String authorizeUrl = "";
		if(url != null && !url.isEmpty() && url.contains("?")) {
			authorizeUrl = url.split("\\?")[1];
		}
		return serverUrl + "/oauth/authorize?" + authorizeUrl;
	}


}
