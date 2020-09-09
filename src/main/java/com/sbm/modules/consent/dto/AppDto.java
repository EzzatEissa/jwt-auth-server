package com.sbm.modules.consent.dto;

import com.sbm.common.dto.BaseDto;

import java.util.List;

public class AppDto extends BaseDto {


    private String clientId;
    private String clientSecret;
    private String authorizedGrantTypes;
    private String registeredRedirectUri;
    private String appName;
    private String fintech;

    private List<PermissionDto> permissions;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public void setRegisteredRedirectUri(String registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFintech() {
        return fintech;
    }

    public void setFintech(String fintech) {
        this.fintech = fintech;
    }
}
