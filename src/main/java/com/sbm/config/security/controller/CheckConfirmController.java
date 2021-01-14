package com.sbm.config.security.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sbm.common.utils.MapperHelper;

/**
 * Created by EzzatEissa on 12/28/2020.
 */

@Controller
@RequestMapping(
		value = "/confirm")
public class CheckConfirmController {

	private static final Logger LOG = LoggerFactory.getLogger(CheckConfirmController.class);
	@Autowired

	private MapperHelper mapperHelper;

	@RequestMapping(
			method = RequestMethod.GET)
	public ResponseEntity<String> auth(HttpServletRequest request, HttpSession session) {

		HttpSession httpSession = request.getSession();
		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();

			byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);

			String credentials = new String(credDecoded, StandardCharsets.UTF_8);

			LOG.info("************************************* credentials " + credentials
					+ "**************************************************");

			final String[] values = credentials.split(":", 2);
			if (values != null && values.length == 2) {

				HttpSession userSession = request.getSession();
				String confirmation = (String) userSession.getAttribute("confirmation");
				String loggedInUserName = (String) userSession.getAttribute("userName");

				LOG.info("************************************* confirmation " + confirmation
						+ "**************************************************");

				LOG.info("************************************* loggedInUserName " + loggedInUserName
						+ "**************************************************");
				String userName = values[0];
				String confirmCode = values[1];

				LOG.info("************************************* userName " + userName
						+ "**************************************************");

				LOG.info("************************************* confirmCode " + confirmCode
						+ "**************************************************");

				if (confirmCode.equals("AB")) {
					LOG.info(
							"************************************* Confirmed successfully u c **************************************************");
					LOG.info(
							"************************************* Confirmed successfully**************************************************");
					return new ResponseEntity<String>("Confirmed successfully", HttpStatus.OK);
				}

				LOG.info(
						"************************************* Not Confirmed successfully**************************************************");
			}

		}

		LOG.info(
				"************************************* unauthorized user **************************************************");
		return new ResponseEntity<String>("unauthorized user", HttpStatus.UNAUTHORIZED);

	}
}
