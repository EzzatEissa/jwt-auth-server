package com.sbm.config.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by EzzatEissa on 12/28/2020.
 */

@Controller
@RequestMapping(value = "/confirm")
public class CheckConfirmController {

    private static final Logger LOG = LoggerFactory.getLogger(CheckConfirmController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> auth(HttpServletRequest request, HttpSession session) {

        LOG.info("************************************* CONFIRMED **************************************************");

        LOG.info("*************************************" + request.getQueryString() +"**************************************************");

        return new ResponseEntity<String>("Confirmed successfully", HttpStatus.OK);
    }
}
