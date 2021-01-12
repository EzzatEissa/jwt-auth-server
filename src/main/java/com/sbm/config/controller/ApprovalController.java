package com.sbm.config.controller;

import com.sbm.config.security.TwoFactorAuthenticationFilter;
import com.sbm.config.security.controller.UserSecurityController;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;

@Controller
@SessionAttributes({ApprovalController.AUTHORIZATION_REQUEST_ATTR_NAME, ApprovalController.ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME})
@RequestMapping
public class ApprovalController{

	static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";

	static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST";

	private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler();

	private static final Logger LOG = LoggerFactory.getLogger(UserSecurityController.class);

	@RequestMapping(value = "/oauth/user-authorize", method = RequestMethod.POST, params = OAuth2Utils.USER_OAUTH_APPROVAL)
	public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
							  SessionStatus sessionStatus, Principal principal, HttpServletResponse httpServletResponse, HttpServletRequest request) {

		HttpSession httpSession = request.getSession();
		if (!(principal instanceof Authentication)) {
			sessionStatus.setComplete();
			throw new InsufficientAuthenticationException(
					"User must be authenticated with Spring Security before authorizing an access token.");
		}

		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get(AUTHORIZATION_REQUEST_ATTR_NAME);

		if (authorizationRequest == null) {
			sessionStatus.setComplete();
			throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
		}

		// Check to ensure the Authorization Request was not modified during the user approval step
		@SuppressWarnings("unchecked")
		Map<String, Object> originalAuthorizationRequest = (Map<String, Object>) model.get(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
		if (isAuthorizationRequestModified(authorizationRequest, originalAuthorizationRequest)) {
			throw new InvalidRequestException("Changes were detected from the original authorization request.");
		}

//		try {
			Set<String> responseTypes = authorizationRequest.getResponseTypes();

			authorizationRequest.setApprovalParameters(approvalParameters);
			authorizationRequest = userApprovalHandler.updateAfterApproval(authorizationRequest,
					(Authentication) principal);
			boolean approved = userApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
			authorizationRequest.setApproved(approved);

			if (authorizationRequest.getRedirectUri() == null) {
				sessionStatus.setComplete();
				throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
			}

			if (!authorizationRequest.isApproved()) {
				return new RedirectView(getUnsuccessfulRedirect(authorizationRequest,
						new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")),
						false, true, false);
			}

//			httpServletResponse.setHeader("Location", "www.google.com");

			if(approvalParameters.get(OAuth2Utils.USER_OAUTH_APPROVAL).toString().equals("true")) {
				httpServletResponse.setStatus(302);
			} else {
				httpServletResponse.setStatus(500);
			}
			String externalUrl = (String)httpSession.getAttribute("externalUrl");
			LOG.info("********************External server url**** "+ externalUrl+ "*********************************************");
			if(externalUrl != null) {
				return new RedirectView(externalUrl,
						false, true, false);
			} else {
				return new RedirectView(getServerUrl(request),
						false, true, false);
			}
//		}
//		finally {
//			sessionStatus.setComplete();
//		}

	}


	@RequestMapping("/oauth/confirm_access")
	public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request, Model mdl) throws Exception {

		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		String clientId = authorizationRequest.getClientId();
		Set<String> scopes = null;
		if (model.containsKey("scope") || request.getAttribute("scope") != null) {
			scopes = authorizationRequest.getScope();
		}

		mdl.addAttribute("clientId", clientId);
		mdl.addAttribute("scopes", scopes);
		mdl.addAttribute("accounts", ((Map)getAccounts().get("Data")).get("Account"));
		return "confirmAccess";
	}

	private Map getAccounts() {
		Map resJson = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("mock", "true");

		HttpEntity<String> entity = new HttpEntity<>("body", headers);

		ResponseEntity<String> res= restTemplate.exchange("https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/accounts", HttpMethod.GET, entity, String.class);



		ObjectMapper mapper = new ObjectMapper();
		try {
			resJson = mapper.readValue(res.getBody(), HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return resJson;
	}


	protected String createTemplate(Map<String, Object> model, HttpServletRequest request) {
		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		String clientId = authorizationRequest.getClientId();

		StringBuilder builder = new StringBuilder();
		builder.append("<html><body><h1>OAuth Approval</h1>");
		builder.append("<p>Do you authorize \"").append(HtmlUtils.htmlEscape(clientId));
		builder.append("\" to access your protected resources?</p>");
		builder.append("<form id=\"confirmationForm\" name=\"confirmationForm\" action=\"");

		String requestPath = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
		if (requestPath == null) {
			requestPath = "";
		}

		builder.append(requestPath).append("/oauth/user-authorize\" method=\"post\">");
		builder.append("<input name=\"user_oauth_approval\" value=\"true\" type=\"hidden\"/>");

		String csrfTemplate = null;
		CsrfToken csrfToken = (CsrfToken) (model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf"));
		if (csrfToken != null) {
			csrfTemplate = "<input type=\"hidden\" name=\"" + HtmlUtils.htmlEscape(csrfToken.getParameterName()) +
					"\" value=\"" + HtmlUtils.htmlEscape(csrfToken.getToken()) + "\" />";
		}
		if (csrfTemplate != null) {
			builder.append(csrfTemplate);
		}

		String authorizeInputTemplate = "<label><input name=\"authorize\" value=\"Authorize\" type=\"submit\"/></label></form>";
		System.out.println(request.getAttributeNames());
		if (model.containsKey("scope") || request.getAttribute("scope") != null) {
			builder.append(createScopes(model, request));
			builder.append(authorizeInputTemplate);
		} else {
			builder.append(authorizeInputTemplate);
			builder.append("<form id=\"denialForm\" name=\"denialForm\" action=\"");
			builder.append(requestPath).append("/oauth/authorize\" method=\"post\">");
			builder.append("<input name=\"user_oauth_approval\" value=\"false\" type=\"hidden\"/>");
			if (csrfTemplate != null) {
				builder.append(csrfTemplate);
			}
			builder.append("<label><input name=\"deny\" value=\"Deny\" type=\"submit\"/></label></form>");
		}

		builder.append("</body></html>");

		return builder.toString();
	}

	private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		StringBuilder builder = new StringBuilder("<ul>");
		@SuppressWarnings("unchecked")
//		Map<String, String> scopes = (Map<String, String>) (model.containsKey("scope") ?
//				model.get("scope") : request.getAttribute("scope"));
		Set<String> scopes = authorizationRequest.getScope();
		for (String scope : scopes) {
			String approved = "checked";
			String denied = "";
			scope = HtmlUtils.htmlEscape(scope);

			builder.append("<li><div class=\"form-group\">");
			builder.append(scope).append(": <input type=\"radio\" name=\"");
			builder.append(scope).append("\" value=\"true\"").append(approved).append(">Approve</input> ");
			builder.append("<input type=\"radio\" name=\"").append(scope).append("\" value=\"false\"");
			builder.append(denied).append(">Deny</input></div></li>");
		}
		builder.append("</ul>");
		return builder.toString();
	}

	private boolean isAuthorizationRequestModified(
			AuthorizationRequest authorizationRequest, Map<String, Object> originalAuthorizationRequest) {
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getClientId(),
				originalAuthorizationRequest.get(OAuth2Utils.CLIENT_ID))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getState(),
				originalAuthorizationRequest.get(OAuth2Utils.STATE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getRedirectUri(),
				originalAuthorizationRequest.get(OAuth2Utils.REDIRECT_URI))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getResponseTypes(),
				originalAuthorizationRequest.get(OAuth2Utils.RESPONSE_TYPE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getScope(),
				originalAuthorizationRequest.get(OAuth2Utils.SCOPE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.isApproved(),
				originalAuthorizationRequest.get("approved"))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getResourceIds(),
				originalAuthorizationRequest.get("resourceIds"))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(
				authorizationRequest.getAuthorities(),
				originalAuthorizationRequest.get("authorities"))) {
			return true;
		}

		return false;
	}

	private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure,
										   boolean fragment) {

		if (authorizationRequest == null || authorizationRequest.getRedirectUri() == null) {
			// we have no redirect for the user. very sad.
			throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
		}

		Map<String, String> query = new LinkedHashMap<String, String>();

		query.put("error", failure.getOAuth2ErrorCode());
		query.put("error_description", failure.getMessage());

		if (authorizationRequest.getState() != null) {
			query.put("state", authorizationRequest.getState());
		}

		if (failure.getAdditionalInformation() != null) {
			for (Map.Entry<String, String> additionalInfo : failure.getAdditionalInformation().entrySet()) {
				query.put(additionalInfo.getKey(), additionalInfo.getValue());
			}
		}

		return append(authorizationRequest.getRedirectUri(), query, fragment);

	}

	private String append(String base, Map<String, ?> query, boolean fragment) {
		return append(base, query, null, fragment);
	}

	private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {

		UriComponentsBuilder template = UriComponentsBuilder.newInstance();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
		URI redirectUri;
		try {
			// assume it's encoded to start with (if it came in over the wire)
			redirectUri = builder.build(true).toUri();
		}
		catch (Exception e) {
			// ... but allow client registrations to contain hard-coded non-encoded values
			redirectUri = builder.build().toUri();
			builder = UriComponentsBuilder.fromUri(redirectUri);
		}
		template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
				.userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

		if (fragment) {
			StringBuilder values = new StringBuilder();
			if (redirectUri.getFragment() != null) {
				String append = redirectUri.getFragment();
				values.append(append);
			}
			for (String key : query.keySet()) {
				if (values.length() > 0) {
					values.append("&");
				}
				String name = key;
				if (keys != null && keys.containsKey(key)) {
					name = keys.get(key);
				}
				values.append(name + "={" + key + "}");
			}
			if (values.length() > 0) {
				template.fragment(values.toString());
			}
			UriComponents encoded = template.build().expand(query).encode();
			builder.fragment(encoded.getFragment());
		}
		else {
			for (String key : query.keySet()) {
				String name = key;
				if (keys != null && keys.containsKey(key)) {
					name = keys.get(key);
				}
				template.queryParam(name, "{" + key + "}");
			}
			template.fragment(redirectUri.getFragment());
			UriComponents encoded = template.build().expand(query).encode();
			builder.query(encoded.getQuery());
		}

		return builder.build().toUriString();

	}

	private String getServerUrl(HttpServletRequest request){

		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String host = url.substring(0, url.indexOf(uri));
		return host;
	}
}

