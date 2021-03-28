package com.sbm.config.controller;

import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.model.Account;
import com.sbm.modules.consent.model.AccountType;
import com.sbm.modules.consent.model.App;
import com.sbm.modules.consent.model.User;
import com.sbm.modules.consent.service.account.AccountTypeService;
import com.sbm.modules.consent.service.account.AccountsService;
import com.sbm.modules.consent.service.app.AppService;
import com.sbm.modules.consent.service.consent.ConsentService;
import com.sbm.modules.consent.service.permission.PermissionService;
import com.sbm.modules.consent.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@SessionAttributes({ ApprovalController.AUTHORIZATION_REQUEST_ATTR_NAME,
		ApprovalController.ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME })
@RequestMapping
public class ApprovalController {

	static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";
	private String approvalParameter = OAuth2Utils.USER_OAUTH_APPROVAL;

	static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST";

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	private static final Logger LOG = LoggerFactory.getLogger(ApprovalController.class);

	@Autowired
	private AppService appService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private ConsentService consentService;

	@Autowired
	AccountsService accountService;

	@Autowired
	UserService userService;

	@Autowired
	private AccountTypeService accountTypeService;

	private final String USER_ACCOUNTS_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/accounts";
	private String accounts = "{\"Data\":{\"Account\":[{\"AccountId\":\"4697356445296\",\"AccountSubType\":\"Mortgage\"},{\"AccountId\":\"1084835731527\",\"AccountSubType\":\"Loan\"}]}}";

	private Boolean isMock = true;

	@RequestMapping(
			value = "/oauth/user-authorize",
			method = RequestMethod.POST,
			params = OAuth2Utils.USER_OAUTH_APPROVAL)
	public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model,
			SessionStatus sessionStatus, Principal principal, HttpServletResponse httpServletResponse,
			HttpServletRequest request) throws  Exception{

		List<String> accounts = null;
		if(approvalParameters.get("accounts") != null) {
			accounts = Arrays.asList(approvalParameters.get("accounts").split(","));
		}

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
		Map<String, Object> originalAuthorizationRequest = (Map<String, Object>) model
				.get(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME);
		if (isAuthorizationRequestModified(authorizationRequest, originalAuthorizationRequest)) {
			throw new InvalidRequestException("Changes were detected from the original authorization request.");
		}

		//		try {
		Set<String> responseTypes = authorizationRequest.getResponseTypes();

		authorizationRequest.setApprovalParameters(approvalParameters);
		authorizationRequest = updateAfterApproval(authorizationRequest,
				(Authentication) principal);

		boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication) principal);
		authorizationRequest.setApproved(approved);

		if (authorizationRequest.getRedirectUri() == null) {
			sessionStatus.setComplete();
			throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
		}

		if (!authorizationRequest.isApproved()) {
			return new RedirectView(getUnsuccessfulRedirect(authorizationRequest,
					new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")), false,
					true, false);
		}

		if (approvalParameters.get(OAuth2Utils.USER_OAUTH_APPROVAL).toString().equals("true")) {
			httpServletResponse.setStatus(302);
		}
		else {
			httpServletResponse.setStatus(500);
		}
		String externalUrl = (String) httpSession.getAttribute("externalUrl");
		LOG.info("********************External server url**** " + externalUrl
				+ "*********************************************");
		if (externalUrl != null) {
			LOG.info("********************External server url done --- **** " + externalUrl
					+ "*********************************************");
			String accountsStr = StringUtils.join(accounts, '+');
			LOG.info("******************* Acounts **** " + accountsStr
					+ "*********************************************");
			httpServletResponse.setHeader("API-OAUTH-METADATA-FOR-PAYLOAD", accountsStr);
			httpServletResponse.setHeader("API-OAUTH-METADATA-FOR-ACCESSTOKEN", accountsStr);
			RedirectView redirectView = new RedirectView(externalUrl, false, true, false);
//			httpServletResponse.sendRedirect(externalUrl);
			return redirectView;
		}
		else {
			return new RedirectView(getServerUrl(request), false, true, false);
		}
	}

	@RequestMapping("/oauth/confirm_access")
	public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request, Model mdl) throws Exception{

		AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
		String clientId = authorizationRequest.getClientId();

		HttpSession userSession = request.getSession();

		App clientDetails = this.appService.getAppByClientId(clientId);

		Set<String> scopes = null;
		if (model.containsKey("scope") || request.getAttribute("scope") != null) {
			scopes = authorizationRequest.getScope();
		}

		List<PermissionDto> permissionDto = this.permissionService.getPermissionsByCodes(new ArrayList<String>(scopes));

		mdl.addAttribute("clientName", clientDetails.getAppName());
		mdl.addAttribute("scopes", permissionDto);

		Map accounts = getAccounts();
		if (accounts != null) {
			Map data = (Map) accounts.get("Data");
			if (data != null && data.get("Account") != null) {
				List accountsList = (List)((Map) accounts.get("Data")).get("Account");
				String userName = (String)userSession.getAttribute("userName");
				if (userName != null && accountsList.size() > 0) {
					accountsList.stream().forEach(account -> {
						if (((Map)account).get("AccountId") != null && !"".equals(((Map)account).get("AccountId").toString().trim())) {
							Account currentAccount = accountService.getAccount(((Map)account).get("AccountId").toString());
							User currentUser = userService.findUserByUsername(userName);

							if ((currentAccount == null || currentAccount.getId() == null) && (currentUser != null && currentUser.getId() != null)) {
								Account accountEntity = new Account();
								accountEntity.setAccountNumber(((Map)account).get("AccountId").toString());
								accountEntity.setUser(currentUser);

								if (((Map)account).get("AccountSubType") != null) {
									AccountType accountType = accountTypeService.getAccountTypeByCode(((Map)account).get("AccountSubType").toString());
									if (accountType != null && accountType.getId() != null) {
										accountEntity.setAccountType(accountType);
									}
								}
								accountService.saveAccount(accountEntity);
							}

						}
					});
				}

				mdl.addAttribute("accounts", ((Map) accounts.get("Data")).get("Account"));
			}
			else {
				mdl.addAttribute("accounts", null);
			}
		}

		return "confirmAccess";
	}


	private Map getAccounts() throws Exception{
		Map resJson = null;
		ObjectMapper mapper = new ObjectMapper();
		if (isMock) {
			resJson = new HashMap();
			Map<String, Object> map = mapper.readValue(accounts, Map.class);
			return map;
		} else {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("mock", "true");

			HttpEntity<String> entity = new HttpEntity<>("body", headers);

			ResponseEntity<String> res = restTemplate.exchange(this.USER_ACCOUNTS_URL, HttpMethod.GET, entity,
					String.class);


			try {
				resJson = mapper.readValue(res.getBody(), HashMap.class);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}



		return resJson;
	}

	private boolean isAuthorizationRequestModified(AuthorizationRequest authorizationRequest,
			Map<String, Object> originalAuthorizationRequest) {
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getClientId(),
				originalAuthorizationRequest.get(OAuth2Utils.CLIENT_ID))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getState(),
				originalAuthorizationRequest.get(OAuth2Utils.STATE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getRedirectUri(),
				originalAuthorizationRequest.get(OAuth2Utils.REDIRECT_URI))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResponseTypes(),
				originalAuthorizationRequest.get(OAuth2Utils.RESPONSE_TYPE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getScope(),
				originalAuthorizationRequest.get(OAuth2Utils.SCOPE))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.isApproved(),
				originalAuthorizationRequest.get("approved"))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResourceIds(),
				originalAuthorizationRequest.get("resourceIds"))) {
			return true;
		}
		if (!ObjectUtils.nullSafeEquals(authorizationRequest.getAuthorities(),
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

	private String getServerUrl(HttpServletRequest request) {

		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String host = url.substring(0, url.indexOf(uri));
		return host;
	}

	private AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
		String flag = approvalParameters.get(approvalParameter);
		boolean approved = flag != null && flag.toLowerCase().equals("true");
		authorizationRequest.setApproved(approved);
		//approve or deny per scope in approvalParameters
		List<String> accounts = null;
		if(approvalParameters.get("accounts") != null) {
			accounts = Arrays.asList(approvalParameters.get("accounts").split(","));
		}
		consentService.save(authorizationRequest, accounts);
		return authorizationRequest;
	}
}
