package com.sbm.modules.consent.dto;

import com.sbm.common.dto.BaseDto;

public class AccountDto extends BaseDto {

    private String accountNumber;

    private UserDto user;

    private AccountTypeDto accountType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public AccountTypeDto getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeDto accountType) {
        this.accountType = accountType;
    }
}
