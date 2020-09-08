package com.sbm.modules.consent.controller;

import com.sbm.modules.consent.dto.AccountDto;
import com.sbm.modules.consent.service.account.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/account")
public class AccountController {

    @Autowired
    private AccountsService accountsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> getAllSegments(@PathVariable("userId") Long userId) {

        List<AccountDto> accounts = accountsService.getAccountsByUserId(userId);

        return new ResponseEntity<List<AccountDto>>(accounts, HttpStatus.OK);
    }
}
