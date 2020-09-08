package com.sbm.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TwoFactorAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(TwoFactorAuthenticationFilter.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public static final String ROLE_TWO_FACTOR_AUTHENTICATED = "ROLE_TWO_FACTOR_AUTHENTICATED";
	public static final String REDIRECT_PATH = "/secure/two_factor_authentication";
    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
				&& !(auth instanceof AnonymousAuthenticationToken)
				&& auth.isAuthenticated()
				&& !hasAuthority(ROLE_TWO_FACTOR_AUTHENTICATED)) {

			if(!request.getRequestURL().toString().contains(REDIRECT_PATH)
					&& !request.getRequestURL().toString().contains("/css/")
					&& !request.getRequestURL().toString().contains("/images")) {
				redirectStrategy.sendRedirect(request, response, REDIRECT_PATH);
				return;
			}
        }

		filterChain.doFilter(request, response);
	}

	private boolean hasAuthority(String checkedAuthority){

    	return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(
                authority -> checkedAuthority.equals(authority.getAuthority())
    			);
    }

	
	
	
}

