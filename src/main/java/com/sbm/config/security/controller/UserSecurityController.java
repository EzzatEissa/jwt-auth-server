package com.sbm.config.security.controller;


import com.sbm.common.dto.UserAuthDto;
import com.sbm.config.security.TwoFactorAuthenticationFilter;
import com.sbm.config.security.service.UrlAuthenticationSuccessHandler;
import com.sbm.config.security.service.UserSecurityService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserSecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityController.class);


    @RequestMapping(method = RequestMethod.GET, value = "/two_factor_authentication")
    public String auth(HttpServletRequest request, HttpSession session) {
        return "loginSecret";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/two_factor_authentication")
    public String auth(@ModelAttribute(value="secret") String secret, BindingResult result, Model model, HttpServletRequest request) {

    	if (userEnteredCorrect2FASecret(secret)) {
            addAuthority(TwoFactorAuthenticationFilter.ROLE_TWO_FACTOR_AUTHENTICATED);
            HttpSession userSession = request.getSession();
            if(userSession != null && userSession.getAttribute("savedUrl") != null && !"".equals(userSession.getAttribute("savedUrl"))) {
                String previousSavedUrl = userSession.getAttribute("savedUrl").toString();
                return "redirect:"+previousSavedUrl;
            } else {
                return "/";
            }
        }
    	
    	model.addAttribute("isIncorrectSecret", true);
        return "loginSecret";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/userlogin")
    public String userLogin(@RequestParam Map<String, String> userCredential, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        UserAuthDto userAuthDto = userSecurityService.userLogin(userCredential.get("userName"), userCredential.get("password"));

        if(userAuthDto.getSuccessLogin()) {
            userSecurityService.setAuthUrlsToSessions(userCredential.get("userName"), response, request);
        }

        if(userAuthDto.getSuccessLogin() && userAuthDto.getSecondFactorEnabled()){

            List<String> secondFactorTypes = userSecurityService.getSecondFactorTypes();
            model.addAttribute("secondFactorTypes", secondFactorTypes);
            return "secondFactorTypes";
        } else {
            return "login";
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/second-factor")
    public String processSecondFactor(@RequestParam Map<String, String> secondFactor, Model model, HttpServletRequest request) {

        // you can generate token or send sms depend on selected second factor.

        return "loginSecret";
    }

    private boolean userEnteredCorrect2FASecret(String secret){
        Boolean isValid = userSecurityService.validateSecondFactor(secret);
    	if(isValid)
    		return true;
    	else;
    		return false;
    }

    private boolean addAuthority(String authority){

        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority newAuthority = new SimpleGrantedAuthority(authority);
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.add(newAuthority);
        updatedAuthorities.addAll(oldAuthorities);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                        SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                        updatedAuthorities)
        );

        return true;
    }

}
