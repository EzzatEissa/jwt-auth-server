package com.sbm.common.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class IbanGenerator {

	private static final BigInteger BD_97 = new BigInteger("97");
	private static final BigInteger BD_98 = new BigInteger("98");
	private static Map<String, String> bankCodesMappings = null;

	static {
		bankCodesMappings = new HashMap<String, String>(24);
		bankCodesMappings.put("RIBLSARI", "20");
		// Adding the same codes ending with xxx
		bankCodesMappings.put("RIBLSARIXXX", "20");
	}


	/**
	 * Translate letters to numbers, also ignoring non alphanumeric characters
	 * 
	 * @param bban
	 * @return the translated value
	 */
	private static String translateChars(final StringBuffer bban) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < bban.length(); i++) {
			char c = bban.charAt(i);
			if (Character.isLetter(c)) {
				result.append(Character.getNumericValue(c));
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @param iban
	 * @return the resulting IBAN
	 */
	public String removeNonAlpha(final String iban) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < iban.length(); i++) {
			char c = iban.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c)) {
				result.append(c);
			}
		}
		return result.toString();
	}

	private static int modulo97(String bban) {
		BigInteger b = new BigInteger(bban);
		b = b.divideAndRemainder(IbanGenerator.BD_97)[1];
		b = IbanGenerator.BD_98.min(b);
		b = b.divideAndRemainder(IbanGenerator.BD_97)[1];
		return b.intValue();
		// return ((int)(98 - (Long.parseLong(bban) * 100) % 97L)) % 97;
	}

	public static String generateRBIBAN(String accountNumber) {
		return generateIBAN("SA", "RIBLSARIXXX", accountNumber);
	}
	public static String generateIBAN(String countryCode, String bankCode, String sAccNo) {
		String sNewIBAN = "";
		StringBuffer sIBAN = new StringBuffer();

		// The check digit creation algorithm...
		// Preliminary step, add country code and empty check digits:

		// add the bank identifier...
		sIBAN.append(bankCodesMappings.get(bankCode));

		// add the branch code...
		// sIBAN.append("0");

		// Add the account number
		if (sAccNo.length() < 18) {
			int remaining = 18 - sAccNo.length();
			String x = "";
			for (int i = 0; i < remaining; i++) {
				x = x + "0";
			}
			sAccNo = x + sAccNo;
		}
		sIBAN.append(sAccNo);

		String sBBAN = sIBAN.toString();

		sIBAN.append(countryCode + "00");
		int nResult = 98 - modulo97(translateChars(sIBAN));

		// zero needs to pad a result less than 10
		String sCheckString;

		if (nResult < 10) {
			sCheckString = "0" + nResult;
		} else {
			sCheckString = "" + nResult;
		}

		sNewIBAN = countryCode + sCheckString + sBBAN;

		return sNewIBAN;

	}
}
