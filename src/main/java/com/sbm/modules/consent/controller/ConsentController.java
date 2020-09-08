package com.sbm.modules.consent.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.sbm.modules.consent.dto.ConsentDto;
import com.sbm.modules.consent.dto.AppDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbm.modules.consent.service.consent.ConsentService;

import java.util.List;

@Controller
@RequestMapping("/admin/account-access-consents")
public class ConsentController {

    @Autowired
    private ConsentService consentService;

    @RequestMapping(value = "/all", method = GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> getAllConsents() {
        return null;
    }

//    @RequestMapping(value = "/user/{userName}", method = GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<List<ConsentDto>> getConsentByUserName(@PathVariable("userName") String userName) {
//        return new ResponseEntity<>(consentService.getConsentByUserName(userName), HttpStatus.OK);
//    }

    @RequestMapping(value = "/user/{userId}", method = GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<List<ConsentDto>> getConsentByUserId(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(consentService.getConsentByUserId(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/app/{userId}", method = GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<List<AppDto>> getConsentAppsByUserId(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(consentService.getConsentAppsForUser(userId), HttpStatus.OK);
    }
}
