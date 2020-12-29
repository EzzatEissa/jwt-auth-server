package com.sbm.config.security.controller;

import com.sbm.common.utils.MapperHelper;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EzzatEissa on 12/28/2020.
 */

@Controller
@RequestMapping(value = "/confirm")
public class CheckConfirmController {

    private static final Logger LOG = LoggerFactory.getLogger(CheckConfirmController.class);
    @Autowired

    private MapperHelper mapperHelper;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> auth(HttpServletRequest request, HttpSession session) {

        LOG.info("************************************* CONFIRMED **************************************************");

        ObjectMapper mapper = new ObjectMapper();
        String requestInfo = null;
        try {
            requestInfo = mapper.writeValueAsString(getRequestInformation(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("*************************************" + requestInfo +"**************************************************");

        final String authorization = request.getHeader("Authorization");
        LOG.info("*************************************" + authorization +"**************************************************");
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            LOG.info("*************************************BASIC**************************************************");
            String base64Credentials = authorization.substring("Basic".length()).trim();

            LOG.info("************************************* base64Credentials " + base64Credentials +"**************************************************");
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);

            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            LOG.info("************************************* credentials " + credentials +"**************************************************");

            final String[] values = credentials.split(":", 2);
             if(values != null && values.length == 2) {

                 HttpSession userSession = request.getSession();
                 String confirmation = (String)userSession.getAttribute("confirmation");
                 String  loggedInUserName = (String)userSession.getAttribute("userName");

                 LOG.info("************************************* confirmation " + confirmation +"**************************************************");

                 LOG.info("************************************* loggedInUserName " + loggedInUserName +"**************************************************");
                 String userName = values[0];
                 String confirmCode = values[1];

                 LOG.info("************************************* userName " + userName +"**************************************************");

                 LOG.info("************************************* confirmCode " + confirmCode +"**************************************************");

                 if(userName.equals(loggedInUserName) && confirmCode.equals(confirmation)) {
                     LOG.info("************************************* Confirmed successfully**************************************************");
                     return new ResponseEntity<String>("Confirmed successfully", HttpStatus.OK);
                 }

                 LOG.info("************************************* Not Confirmed successfully**************************************************");
             }


        }

        LOG.info("************************************* unauthorized user **************************************************");
        return new ResponseEntity<String>("unauthorized user", HttpStatus.UNAUTHORIZED);

    }


    private Map<String, String> getRequestInformation(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put("header: " + key, value);
        }
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put("parameter: " + key, value);
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                map.put("cookie: " + cookie.getName(), cookie.getValue());

            }
        }

        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put("parameter: " + key, value);
        }
        map.put("getRemoteUser", request.getRemoteUser());
        map.put("getMethod", request.getMethod());
        map.put("getQueryString", request.getQueryString());
        map.put("getAuthType", request.getAuthType());
        map.put("getContextPath", request.getContextPath());
        map.put("getPathInfo", request.getPathInfo());
        map.put("getPathTranslated", request.getPathTranslated());
        map.put("getRequestedSessionId", request.getRequestedSessionId());
        map.put("getRequestURI", request.getRequestURI());
        map.put("getRequestURL", request.getRequestURL().toString());
        map.put("getMethod", request.getMethod());
        map.put("getServletPath", request.getServletPath());
        map.put("getContentType", request.getContentType());
        map.put("getLocalName", request.getLocalName());
        map.put("getProtocol", request.getProtocol());
        map.put("getRemoteAddr", request.getRemoteAddr());
        map.put("getServerName", request.getServerName());
        return map;
    }
}
