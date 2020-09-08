package com.sbm.modules.consent.model;

import com.sbm.common.model.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Account extends BaseEntity {


    private String accountNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "account")
    private Set<Consent> consents;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "account_app", joinColumns = {
            @JoinColumn(name = "account_id", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "app_id", nullable = false, updatable = false) })
    private List<App> apps;

    @ManyToOne
    private AccountType accountType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Set<Consent> getConsents() {
        return consents;
    }

    public void setConsents(Set<Consent> consents) {
        this.consents = consents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
