package com.sbm.config.security.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sbm.modules.consent.dto.AccountDto;
import com.sbm.modules.consent.dto.ConsentDto;
import com.sbm.modules.consent.dto.UserDto;
import com.sbm.modules.consent.model.Account;
import com.sbm.modules.consent.service.account.AccountsService;
import com.sbm.modules.consent.service.consent.ConsentService;
import com.sbm.modules.consent.service.user.UserService;
import com.sbm.modules.openbanking.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

	@Autowired
	private ConsentService consentService;

	@Autowired
	UserService userService;

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

				String userName = values[0];
				String confirmCode = values[1];

				LOG.info("************************************* userName " + userName
						+ "**************************************************");

				LOG.info("************************************* confirmCode " + confirmCode
						+ "**************************************************");

				if (confirmCode.equals("AB")) {
					UserDto userDto = userService.getUserByUsername(userName);
					LOG.info("************************************* USERNAME  **************************************************" + userDto.getFirstName());
					List<ConsentDto> consents = consentService.getConsentByUserId(userDto.getId());
					List<String> accounts = new ArrayList<>();
					if(consents != null && !consents.isEmpty()){
						consents.stream().forEach(consent -> {
							accounts.add(consent.getAccount().getAccountNumber());
						});
					}
					LOG.info(
							"************************************* Confirmed successfully u c **************************************************");
					LOG.info(
							"************************************* Confirmed successfully**************************************************");
					String accountsStr = StringUtils.join(accounts, '+');
					LOG.info("******************* Acounts confirm**** " + accountsStr
							+ "*********************************************");
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.set("API-OAUTH-METADATA-FOR-PAYLOAD", accountsStr);
					responseHeaders.set("API-OAUTH-METADATA-FOR-ACCESSTOKEN", accountsStr);
					return ResponseEntity.ok().headers(responseHeaders).body("Confirmed successfully");
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
