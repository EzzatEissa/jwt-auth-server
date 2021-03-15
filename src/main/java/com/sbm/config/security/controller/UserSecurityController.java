package com.sbm.config.security.controller;

import com.sbm.common.dto.UserAuthDto;
import com.sbm.config.security.TwoFactorAuthenticationFilter;
import com.sbm.config.security.service.UserSecurityService;
import com.sbm.modules.consent.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(
		value = "/user")
public class UserSecurityController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	private static final Logger LOG = LoggerFactory.getLogger(UserSecurityController.class);

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/two_factor_authentication")
	public String auth(HttpServletRequest request, HttpSession session) {
		return "loginSecret";
	}

	@RequestMapping(
			method = RequestMethod.POST,
			value = "/two_factor_authentication")
	public String auth(@RequestParam Map<String, String> confirmSecret, Model model, HttpServletRequest request) {
		String secret = confirmSecret.get("secret");
		String secondFactorType = confirmSecret.get("secondFactorType");

		HttpSession userSession = request.getSession();
		String userName = (String) userSession.getAttribute("userName");

		if (this.userSecurityService.validateSecondFactor(secret, secondFactorType, userName)
				&& autoLoginAndAddAuthority(TwoFactorAuthenticationFilter.ROLE_TWO_FACTOR_AUTHENTICATED, request)) {
			if (userSession != null && userSession.getAttribute("savedUrl") != null
					&& !"".equals(userSession.getAttribute("savedUrl"))) {
				String previousSavedUrl = userSession.getAttribute("savedUrl").toString();
				return "redirect:" + previousSavedUrl;
			}
			else {
				return "/";
			}
		}

		model.addAttribute("errMsg", "Error");
		return "/login";
	}

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "login";
	}

	@RequestMapping(
			method = RequestMethod.POST,
			value = "/userlogin")
	public String userLogin(@RequestParam Map<String, String> userCredential, Model model, HttpServletRequest request,
							HttpServletResponse response) throws Exception {
		UserAuthDto userAuthDto = null;
		try {
			userAuthDto = this.userSecurityService.userLogin(userCredential.get("username"),
					userCredential.get("password"), request);
		} catch (Exception ex) {
			if (ex.getMessage().equals("ErrorLogin")) {
				model.addAttribute("errMsg", "ErrorLogin");
			} else {
				model.addAttribute("errMsg", "Error");
			}

			return "/login";
		}
		if (userAuthDto.getSuccessLogin() != null && userAuthDto.getSuccessLogin()) {
			this.userSecurityService.setAuthUrlsToSessions(userCredential.get("username"), response, request);
		}

		if (userAuthDto.getSuccessLogin() && userAuthDto.getSecondFactorEnabled()) {
			model.addAttribute("secondFactorTypes", userAuthDto.getAuthenticationFactorTypesDtoList());
			return "secondFactorTypes";
		}
		else {
			model.addAttribute("usernamePasswordError", "incorrect");
			return "/login";
		}

	}

	@RequestMapping(
			method = RequestMethod.POST,
			value = "/second-factor")
	public String processSecondFactor(@RequestParam Map<String, String> secondFactor, Model model,
			HttpServletRequest request) {
		HttpSession userSession = request.getSession();

		String userName = (String) userSession.getAttribute("userName");
		// you can generate token or send sms depend on selected second factor.
		String OTPValue = this.userSecurityService.generateOTP(secondFactor.get("secondFactorType"), userName,false);
		if(OTPValue != null) {
			HttpSession session = request.getSession();
			session.setAttribute("OTPValue", OTPValue);
			model.addAttribute("secondFactorType", secondFactor.get("secondFactorType"));
			return "loginSecret";
		} else {
			model.addAttribute("errMsg", "Error");
			return "/login";
		}


	}

	private boolean autoLoginAndAddAuthority(String authority, HttpServletRequest request) {

		HttpSession userSession = request.getSession();

		String userName = (String) userSession.getAttribute("userName");
		String password = (String) userSession.getAttribute("password");
		if (userName != null && password != null) {

			Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
					.getContext().getAuthentication().getAuthorities();
			SimpleGrantedAuthority newAuthority = new SimpleGrantedAuthority(authority);
			List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
			updatedAuthorities.add(newAuthority);
			updatedAuthorities.addAll(oldAuthorities);
			Authentication auth = new UsernamePasswordAuthenticationToken(userName, password, updatedAuthorities);
			SecurityContextHolder.getContext().setAuthentication(auth);

			SecurityContext sc = SecurityContextHolder.getContext();
			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
			return true;
		}
		else {
			return false;
		}

	}

}
