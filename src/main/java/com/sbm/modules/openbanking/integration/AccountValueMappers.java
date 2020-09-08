package com.sbm.modules.openbanking.integration;

public class AccountValueMappers {
	public static String mapType(String rbAccountType) {
		if ("CA".equals(rbAccountType)) {
			return "CurrentAccount";
		} else if ("SA".equals(rbAccountType)) {
			return "Savings";
		} else if ("OD".equals(rbAccountType)) {
			return "OverDraft";
		} else if ("LO".equals(rbAccountType)) {
			return "Loan";
		} else if ("CC".equals(rbAccountType)) {
			return "CreditCard";
		} else {
			return rbAccountType;
		}
	}
	
	public static String mapStatus(final String rbAccountStatus) {
		if ("00".equals(rbAccountStatus)) {
			return "Enabled";
		} else if ("01".equals(rbAccountStatus) || "02".equals(rbAccountStatus) || "06".equals(rbAccountStatus)
				|| "07".equals(rbAccountStatus) || "08".equals(rbAccountStatus) || "09".equals(rbAccountStatus)) {
			return "Disabled";
		} else if ("03".equals(rbAccountStatus) || "04".equals(rbAccountStatus) || "05".equals(rbAccountStatus)) {
			return "Deleted";
		} else {
			return rbAccountStatus;
		}
	}
}
