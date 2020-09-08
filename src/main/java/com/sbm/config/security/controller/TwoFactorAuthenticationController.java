package com.sbm.config.security.controller;


import com.sbm.config.security.TwoFactorAuthenticationFilter;
import com.sbm.modules.consent.model.User;
import com.sbm.modules.consent.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/secure/two_factor_authentication")
public class TwoFactorAuthenticationController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String auth(HttpServletRequest request, HttpSession session) {
        return "loginSecret";
    }

    @RequestMapping(method = RequestMethod.POST)
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

    private boolean userEnteredCorrect2FASecret(String secret){
        User user = userService.getUserByMobileNumber(secret);
    	if(secret != null && user != null && secret.equals(user.getMobile()))
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
