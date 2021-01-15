package com.sbm.config.security.service;

import com.sbm.modules.consent.service.app.AppService;
import com.sbm.modules.consent.service.consent.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;

import java.util.Map;

public class CustomUserApprovalHandler extends DefaultUserApprovalHandler{
	private String approvalParameter = OAuth2Utils.USER_OAUTH_APPROVAL;

	@Autowired
	private ConsentService consentService;

	@Autowired
	private AppService appService;
	
//	@Override
//	public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
//		Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
//		String flag = approvalParameters.get(approvalParameter);
//		boolean approved = flag != null && flag.toLowerCase().equals("true");
//		authorizationRequest.setApproved(approved);
//		//approve or deny per scope in approvalParameters
//		consentService.save(authorizationRequest);
//		return authorizationRequest;
//	}
}
