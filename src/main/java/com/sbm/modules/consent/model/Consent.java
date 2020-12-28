package com.sbm.modules.consent.model;

import com.sbm.common.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
//@Table(uniqueConstraints={
//        @UniqueConstraint(columnNames = {"app", "account"})
//})
public class Consent extends BaseEntity {

    @ManyToMany
    @JoinTable(name = "consent_permission", joinColumns = {
            @JoinColumn(name = "consent_id", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
    private List<Permission> permissions;

    @ManyToOne
    private App app;

    @ManyToOne
    private Account account;

    private Date expirationDateTime;

    private Date transactionFromDateTime;

    private Date transactionToDateTime;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
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

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void addPermission(Permission perm) {
        this.permissions.add(perm);
    }

    public void removePermission(Permission perm) {
        this.permissions.remove(perm);
    }
}
