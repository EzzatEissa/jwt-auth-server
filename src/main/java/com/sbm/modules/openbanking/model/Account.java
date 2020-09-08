package com.sbm.modules.openbanking.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class Account implements OpenBankingType {

	private static final long serialVersionUID = 8020465379022036989L;

	private String accountId;

	private String status; // TODO: enum

	private String currency;

	private String accountType; // TODO: enum

	private String accountSubType; // TODO: enum

	private String nickname;

	private AccountInfo account;

	private Servicer servicer;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountSubType() {
		return accountSubType;
	}

	public void setAccountSubType(String accountSubType) {
		this.accountSubType = accountSubType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public AccountInfo getAccount() {
		return account;
	}

	public void setAccount(AccountInfo account) {
		this.account = account;
	}

	public Servicer getServicer() {
		return servicer;
	}

	public void setServicer(Servicer servicer) {
		this.servicer = servicer;
	}

	@Data
	public static class AccountInfo implements Serializable{

		private static final long serialVersionUID = 7456774183425025064L;

		private String name;

		private String schemeName;

		private String identification;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSchemeName() {
			return schemeName;
		}

		public void setSchemeName(String schemeName) {
			this.schemeName = schemeName;
		}

		public String getIdentification() {
			return identification;
		}

		public void setIdentification(String identification) {
			this.identification = identification;
		}
		
		
	}

	@Data
	public static class Servicer {

		private String schemeName;

		private String identification;

		public String getSchemeName() {
			return schemeName;
		}

		public void setSchemeName(String schemeName) {
			this.schemeName = schemeName;
		}

		public String getIdentification() {
			return identification;
		}

		public void setIdentification(String identification) {
			this.identification = identification;
		}
		
		
	}

}
