package com.sbm.common.security;

import com.sbm.modules.consent.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hesham on 3/22/16.
 */
public class SecurityUtils {




    /**TODO Verify thread-safety
     * @return
     */
	@Deprecated
	public static String getCurrentAppUrl() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			/*int serverPort = request.getServerPort();
			String serverPortString = serverPort != -1 ? ":" + serverPort : "";*/
			String url = "http://" + request.getServerName();
			return url;
		}
		return null;
	}

	@Deprecated
	public static String getCurrentAppUrlForUI() {
		String url = getCurrentAppUrl() != null ? getCurrentAppUrl() + "UI" : null;
		return url;
	}

	public static void configureSecurityContextForAuthenticatedUser(User authenticatedUser){
		Authentication newAuth = new UsernamePasswordAuthenticationToken(authenticatedUser, authenticatedUser.getPassword(), authenticatedUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	public static String appendNationalIdToPassword(String password, String national_id) {
		return password.concat(national_id.toString().substring(national_id.toString().length() - 4, national_id.toString().length()));
	}

}
