package com.sbm.config.security.service;

import com.sbm.common.dto.UserAuthDto;
import com.sbm.common.security.SecurityUtils;
import com.sbm.config.security.controller.UserSecurityController;
import com.sbm.modules.consent.model.User;
import com.sbm.modules.consent.service.user.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EzzatEissa on 1/7/2021.
 */

@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityController.class);
    private final String LOGIN_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/login";
    private final String SECOND_FACTOR_TYPES_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/get-second-factor-authentication-methods";
    private final String VALIDATE_SECOND_FACTOR_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/validate-second-factor-authentication";

    private static final String REDIRECT_PATH = "/user/second_factor";

    @Autowired
    UserService userService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public UserAuthDto userLogin(String userName, String password) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("mock", "true");

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> res = restTemplate.exchange(LOGIN_URL, HttpMethod.POST, entity, String.class);


        if (res != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                UserAuthDto userAuthDto = new UserAuthDto();
                if(resJson.get("data") != null && ((Map)resJson.get("data")).get("success-login") != null) {
                    userAuthDto.setSuccessLogin(Boolean.parseBoolean((((Map)resJson.get("data")).get("success-login")).toString().toLowerCase()));
                }

                if(resJson.get("data") != null && ((Map)resJson.get("data")).get("secondary-authentication-enabled") != null) {
                    userAuthDto.setSecondFactorEnabled(Boolean.parseBoolean((((Map)resJson.get("data")).get("secondary-authentication-enabled")).toString().toLowerCase()));
                }

                return userAuthDto;
            } catch (IOException e) {
                e.printStackTrace();
                return new UserAuthDto();
            }


        } else {
            return new UserAuthDto();
        }
    }

    @Override
    public List<String> getSecondFactorTypes() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("mock", "true");

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> res = restTemplate.exchange(SECOND_FACTOR_TYPES_URL, HttpMethod.POST, entity, String.class);


        if (res != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                if(resJson.get("data") != null && ((Map)resJson.get("data")).get("authentication-methods") != null) {
                    return (List)((Map)resJson.get("data")).get("authentication-methods");
                }

                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        } else {
            return null;
        }
    }

    @Override
    public Boolean validateSecondFactor(String confirmCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("mock", "true");

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> res = restTemplate.exchange(VALIDATE_SECOND_FACTOR_URL, HttpMethod.POST, entity, String.class);


        if (res != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                if(resJson.get("data") != null && ((Map)resJson.get("data")).get("success-validation") != null) {
                    return Boolean.parseBoolean(((Map)resJson.get("data")).get("success-validation").toString().toLowerCase());
                }

                return Boolean.FALSE;
            } catch (IOException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }


        } else {
            return Boolean.FALSE;
        }
    }

    private void setLoggedInUserInContext(String userName) {
        try {
            User user = userService.findUserByUsername(userName);
            if (user != null) {
                SecurityUtils.configureSecurityContextForAuthenticatedUser(user);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String getServerUrl(HttpServletRequest request){

        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        LOG.info("------------------------getServerUrl ------------------ "+ host);
        return host;
    }

    private String composeAuthorizeUrl(String url, String serverUrl) {
        String authorizeUrl = "";
        LOG.info("------------------------Url Url Url Url ------------------ "+ url);
        if(url != null && !url.isEmpty() && url.contains("?")) {
            authorizeUrl = url.split("\\?")[1];
        }
        LOG.info("------------------------composeAuthorizeUrl ------------------ "+ authorizeUrl);
        return serverUrl + "/oauth/authorize?" + authorizeUrl;
    }

    @Override
    public void setAuthUrlsToSessions(String userName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        setLoggedInUserInContext(userName);
        HttpSession userSession = request.getSession();
        if(userSession != null && userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null && !"".equals(userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST"))) {
            SavedRequest previousSavedUrl = (SavedRequest)userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            String oldUrl = previousSavedUrl.getRedirectUrl();
            if(oldUrl != null && oldUrl.contains("original-url")) {
                String externalUrl = oldUrl.split("original-url=")[1];
                String serverUrl = getServerUrl(request);
                String authorizeUrl = composeAuthorizeUrl(URLDecoder.decode( externalUrl.toString(), "UTF-8" ), serverUrl);
                StringBuilder location = new StringBuilder();

                location.append(externalUrl)
                        .append("&username=")
                        .append(userName)
                        .append("&confirmation=")
                        .append("AB");


                userSession.setAttribute("confirmation", "AB");
                userSession.setAttribute("userName", userName);
                userSession.setAttribute("externalUrl", URLDecoder.decode( location.toString(), "UTF-8" ));
                System.out.println("Redirect location original = " + URLDecoder.decode( location.toString(), "UTF-8" ));
                LOG.info("Redirect location original = " + URLDecoder.decode( location.toString(), "UTF-8" ));

                LOG.info("Redirect location original = " + URLDecoder.decode( authorizeUrl.toString(), "UTF-8" ));
                userSession.setAttribute("savedUrl",URLDecoder.decode( authorizeUrl.toString(), "UTF-8" ));

                //http://localhost:8080/oauth/authorize?response_type=code&client_id=d6492371-762b-4768-937f-6be6b3cec29f&scope=ReadAccountsBasic+ReadAccountsDetail&redirect_uri=https://www.info-tech.com/app-callback

            } else {
                userSession.setAttribute("savedUrl",URLDecoder.decode( oldUrl.toString(), "UTF-8" ));
            }

            System.out.println("without original url ===== == == " + previousSavedUrl.getRedirectUrl());
        }
    }
}
