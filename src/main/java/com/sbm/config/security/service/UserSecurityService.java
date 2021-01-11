package com.sbm.config.security.service;

import com.sbm.common.dto.UserAuthDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by EzzatEissa on 1/7/2021.
 */
public interface UserSecurityService {

    UserAuthDto userLogin(String userName, String password);

    List<String> getSecondFactorTypes();

    Boolean validateSecondFactor(String confirmCode);

    void setAuthUrlsToSessions(String userName, HttpServletResponse response, HttpServletRequest request) throws Exception;
}
