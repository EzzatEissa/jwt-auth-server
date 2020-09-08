package com.sbm.config.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2RequestFactory extends DefaultOAuth2RequestFactory {


    @Autowired
    private ClientDetailsService clientDetailsService;

    private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();

    private boolean checkUserScopes = false;

    public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }

//    public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
//
//        String clientId = authorizationParameters.get(OAuth2Utils.CLIENT_ID);
//        String state = authorizationParameters.get(OAuth2Utils.STATE);
//        String redirectUri = authorizationParameters.get(OAuth2Utils.REDIRECT_URI);
//        Set<String> responseTypes = OAuth2Utils.parseParameterList(authorizationParameters
//                .get(OAuth2Utils.RESPONSE_TYPE));
//
//        Set<String> scopes = extractScopes(authorizationParameters, clientId);
//
//        AuthorizationRequest request = new AuthorizationRequest(authorizationParameters,
//                Collections.<String, String> emptyMap(), clientId, scopes, null, null, false, state, redirectUri,
//                responseTypes);
//
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//        request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);
//
//        return request;
//
//    }
//
//    private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
//        Set<String> scopes = OAuth2Utils.parseParameterList(requestParameters.get(OAuth2Utils.SCOPE));
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//        if ((scopes == null || scopes.isEmpty())) {
//            // If no scopes are specified in the incoming data, use the default values registered with the client
//            // (the spec allows us to choose between this option and rejecting the request completely, so we'll take the
//            // least obnoxious choice as a default).
//            scopes = clientDetails.getScope();
//        }
//
//        if (checkUserScopes) {
//            scopes = checkUserScopes(scopes, clientDetails);
//        }
//        return scopes;
//    }
//
//    private Set<String> checkUserScopes(Set<String> scopes, ClientDetails clientDetails) {
//        if (!securityContextAccessor.isUser()) {
//            return scopes;
//        }
//        Set<String> result = new LinkedHashSet<String>();
//        Set<String> authorities = AuthorityUtils.authorityListToSet(securityContextAccessor.getAuthorities());
//        for (String scope : scopes) {
//            if (authorities.contains(scope) || authorities.contains(scope.toUpperCase())
//                    || authorities.contains("ROLE_" + scope.toUpperCase())) {
//                result.add(scope);
//            }
//        }
//        return result;
//    }

}

