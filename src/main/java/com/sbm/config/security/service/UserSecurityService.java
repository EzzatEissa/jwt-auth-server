package com.sbm.config.security.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sbm.common.dto.UserAuthDto;

/**
 * Created by EzzatEissa on 1/7/2021.
 */
public interface UserSecurityService {

	UserAuthDto userLogin(String userName, String password, HttpServletRequest request) throws Exception;

	Boolean validateSecondFactor(String confirmCode, String secondFactorType, String userName);

	void setAuthUrlsToSessions(String userName, HttpServletResponse response, HttpServletRequest request)
			throws Exception;
	String generateOTP(String secondFactorType, String userName, Boolean isRegenerateCode);
}
