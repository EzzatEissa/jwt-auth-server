package com.sbm.modules.consent.model;

public class CustomClient {


	private String clientId;
	private String resourceIds;
	private String clientSecret;
	private String scope;
	private String authorizedGranttypes;
	private String registeredRedirectUri;
	private String authorities;
	private String accessTokenValiditySeconds;
	private String refreshTokenValiditySeconds;
	private String additionalInformation;
	private String autoapprove;
	
//	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
//    @JoinTable(name = "client_permission", joinColumns = {
//            @JoinColumn(name = "client_id", nullable = false, updatable = false) }, inverseJoinColumns = {
//            @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
//    private Set<Permission> permissions;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorizedGranttypes() {
		return authorizedGranttypes;
	}

	public void setAuthorizedGranttypes(String authorizedGranttypes) {
		this.authorizedGranttypes = authorizedGranttypes;
	}

	public String getRegisteredRedirectUri() {
		return registeredRedirectUri;
	}

	public void setRegisteredRedirectUri(String registeredRedirectUri) {
		this.registeredRedirectUri = registeredRedirectUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(String accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public String getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(String refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}

//	public Set<Permission> getPermissions() {
//		return permissions;
//	}
//
//	public void setPermissions(Set<Permission> permissions) {
//		this.permissions = permissions;
//	}
	
	

}
