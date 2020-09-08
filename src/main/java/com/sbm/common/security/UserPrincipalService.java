package com.sbm.common.security;

import com.sbm.modules.consent.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class UserPrincipalService {

	public User getCurrentAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null){
			return null;
		}

		if (! auth.isAuthenticated()){
			return null;
		}

		if (isAnonymousUser(auth)){
			return null;
		}

		return getUserFromPrincipal(auth.getPrincipal());
	}
	
	public Long getCurrentAuthenticatedId() {
		User user = getCurrentAuthenticatedUser();
		if (user == null){
			return null;
		}
		return user.getId();
	}

	/**org.springframework.security.web.authentication.AnonymousAuthenticationFilter
	 * @param auth
	 * @return
	 */
	public boolean isAnonymousUser(Authentication auth){
		if (auth == null)
			return true;

		if (auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))
			return true;

		return false;
	}

	public User getUserFromPrincipal(Object principal) {
		if (principal == null){
			throw new IllegalArgumentException("Supplied principal is null");
		}
		if (principal instanceof User) {
			// Currently this includes SecurityUserDetails.class and LdapUserDetails.class
			String username = ((UserDetails) principal).getUsername();

			if (username.isEmpty()) {
				return null;
			}

			return	(User) principal;
		}


		throw new IllegalArgumentException("Supplied Principal is of an unsupported type: " + principal.getClass() + ", toString(): " + principal.toString());
	}

}