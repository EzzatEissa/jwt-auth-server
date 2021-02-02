package com.sbm.config.security.service;

import com.sbm.common.dto.AuthenticationFactorTypesDto;
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
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EzzatEissa on 1/7/2021.
 */

@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityController.class);
    //    private final String LOGIN_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/login";
    private final String LOGIN_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/login";

    private final String GENERATE_OTP_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/generateOTP";
    private final String REGENERATE_OTP_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/resendOTP";

    private final String VALIDATE_CODE_URL = "https://api.eu-gb.apiconnect.appdomain.cloud/marehemsbmcomsa-dev/test-catalog/validateCode";

    private static final String REDIRECT_PATH = "/user/second_factor";

    @Autowired
    UserService userService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public UserAuthDto userLogin(String userName, String password, HttpServletRequest request) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = new HashMap<>();
        body.put("username", userName);
        body.put("password", "P@ssw0rd1");
        headers.add("mock", "false");

        headers.add("x-rb-user-id", userName);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> res = null;
        try {
            res = restTemplate.exchange(this.LOGIN_URL, HttpMethod.POST, entity, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Connection timeout");
        }

        if (res != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                if (resJson.get("data") != null) {
                    UserAuthDto userAuthDto = new UserAuthDto();
                    Boolean successLogin = (Boolean) ((Map) resJson.get("data")).get("success");
                    if (successLogin != null && successLogin) {
                        userAuthDto.setSuccessLogin(successLogin);
                        HttpSession userSession = request.getSession();
                        userSession.setAttribute("userName", userName);
                        userSession.setAttribute("password", password);
                        Boolean secondAuthEnabled = (Boolean) ((Map) resJson.get("data")).get("secondary_authentication_enabled");
                        if (secondAuthEnabled != null) {
                            userAuthDto.setSecondFactorEnabled(secondAuthEnabled);
                            if (((Map) resJson.get("data")).get("authentication_types") != null) {
                                List<AuthenticationFactorTypesDto> authenticationFactorTypesDtoList = (List) ((Map) resJson.get("data")).get("authentication_types");
                                userAuthDto.setAuthenticationFactorTypesDtoList(authenticationFactorTypesDtoList);
                            }

                        }
                        return userAuthDto;
                    } else {
                        throw new Exception("ErrorLogin");
                    }


                }
                throw new Exception("Error");
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("Error");
            }

        } else {
            throw new Exception("Error");
        }
    }

    @Override
    public Boolean validateSecondFactor(String confirmCode, String secondFactorType, String userName) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = new HashMap<>();
        body.put("authentication_type", secondFactorType);
          if ("SMS_PRIMARY".equals(secondFactorType) || "SMS_SECONDARY".equals(secondFactorType)) {
            body.put("OTP", confirmCode);
        } else if ("TOKEN".equals(secondFactorType) || "SOFTTOKEN".equals(secondFactorType)) {
            body.put("token", confirmCode);
        }


        headers.add("mock", "false");

        headers.add("x-rb-user-id", userName);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> res = restTemplate.exchange(this.VALIDATE_CODE_URL, HttpMethod.POST, entity,
                    String.class);
            if (res != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                if (resJson.get("data") != null && ((Map) resJson.get("data")).get("success") != null) {
                    return Boolean.parseBoolean(
                            ((Map) resJson.get("data")).get("success").toString().toLowerCase());
                }

                return Boolean.FALSE;


            } else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private void setLoggedInUserInContext(String userName) {
        try {
            User user = this.userService.findUserByUsername(userName);
            if (user != null) {
                SecurityUtils.configureSecurityContextForAuthenticatedUser(user);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String getServerUrl(HttpServletRequest request) {

        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        LOG.info("------------------------getServerUrl ------------------ " + host);
        return host;
    }

    private String composeAuthorizeUrl(String url, String serverUrl) {
        String authorizeUrl = "";
        LOG.info("------------------------Url Url Url Url ------------------ " + url);
        if (url != null && !url.isEmpty() && url.contains("?")) {
            authorizeUrl = url.split("\\?")[1];
        }
        LOG.info("------------------------composeAuthorizeUrl ------------------ " + authorizeUrl);
        return serverUrl + "/oauth/authorize?" + authorizeUrl;
    }

    @Override
    public void setAuthUrlsToSessions(String userName, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        setLoggedInUserInContext(userName);
        HttpSession userSession = request.getSession();
        if (userSession != null && userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null
                && !"".equals(userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST"))) {
            SavedRequest previousSavedUrl = (SavedRequest) userSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            String oldUrl = previousSavedUrl.getRedirectUrl();
            if (oldUrl != null && oldUrl.contains("original-url")) {
                String externalUrl = oldUrl.split("original-url=")[1];
                String serverUrl = getServerUrl(request);
                String authorizeUrl = composeAuthorizeUrl(URLDecoder.decode(externalUrl.toString(), "UTF-8"),
                        serverUrl);
                StringBuilder location = new StringBuilder();

                location.append(externalUrl).append("&username=").append(userName).append("&confirmation=")
                        .append("AB");

                userSession.setAttribute("confirmation", "AB");
                userSession.setAttribute("userName", userName);
                userSession.setAttribute("externalUrl", URLDecoder.decode(location.toString(), "UTF-8"));
                System.out.println("Redirect location original = " + URLDecoder.decode(location.toString(), "UTF-8"));
                LOG.info("Redirect location original = " + URLDecoder.decode(location.toString(), "UTF-8"));

                LOG.info("Redirect location original = " + URLDecoder.decode(authorizeUrl.toString(), "UTF-8"));
                userSession.setAttribute("savedUrl", URLDecoder.decode(authorizeUrl.toString(), "UTF-8"));

                //http://localhost:8080/oauth/authorize?response_type=code&client_id=d6492371-762b-4768-937f-6be6b3cec29f&scope=ReadAccountsBasic+ReadAccountsDetail&redirect_uri=https://www.info-tech.com/app-callback

            } else {
                userSession.setAttribute("confirmation", "AB");
                userSession.setAttribute("userName", userName);
                userSession.setAttribute("savedUrl", URLDecoder.decode(oldUrl.toString(), "UTF-8"));
            }

            System.out.println("without original url ===== == == " + previousSavedUrl.getRedirectUrl());
        }
    }

    @Override
    public String generateOTP(String secondFactorType, String userName, Boolean isRegenerateCode) {
        String OTP = null;
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = new HashMap<>();
        body.put("authentication_type", secondFactorType);

        headers.add("mock", "false");

        headers.add("x-rb-user-id", userName);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> res = restTemplate.exchange((isRegenerateCode) ? this.REGENERATE_OTP_URL : this.GENERATE_OTP_URL, HttpMethod.POST, entity,
                    String.class);


            if (res != null) {
                ObjectMapper mapper = new ObjectMapper();

                Map resJson = mapper.readValue(res.getBody(), HashMap.class);
                if (resJson.get("data") != null && ((Map) resJson.get("data")).get("success") != null) {
                    return ((Map) resJson.get("data")).get("success").toString().toLowerCase();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OTP;
    }
}
