package com.sbm.modules.consent.dto;

import com.sbm.common.dto.BaseDto;

import java.util.Date;
import java.util.List;

public class ConsentDto extends BaseDto {

    private List<PermissionDto> permissions;

    private AppDto app;

    private AccountDto account;

    private Date expirationDateTime;

    private Date transactionFromDateTime;

    private Date transactionToDateTime;

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }

    public AppDto getApp() {
        return app;
    }

    public void setApp(AppDto app) {
        this.app = app;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    public Date getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(Date expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public Date getTransactionFromDateTime() {
        return transactionFromDateTime;
    }

    public void setTransactionFromDateTime(Date transactionFromDateTime) {
        this.transactionFromDateTime = transactionFromDateTime;
    }

    public Date getTransactionToDateTime() {
        return transactionToDateTime;
    }

    public void setTransactionToDateTime(Date transactionToDateTime) {
        this.transactionToDateTime = transactionToDateTime;
    }
}
