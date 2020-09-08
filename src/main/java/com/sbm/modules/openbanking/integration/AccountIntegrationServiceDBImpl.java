package com.sbm.modules.openbanking.integration;


import com.sbm.modules.openbanking.model.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountIntegrationServiceDBImpl implements AccountIntegrationService {

	
	@Override
	public List<Account> getAccounts(String userId, String accountNo) {
		List<Account> accounts = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountId("1111111");
		account.setCurrency("SAR");
		account.setNickname("Nick");
		account.setStatus("status");
		accounts.add(account);
		return accounts;
	}
	
//	public List<Account> getAccounts2(String userId, String accountNo) {
//		Map<String, Object> resultMap = null;
//		try {
//
//			//conn =ds.getConnection();
//			//conn.setAutoCommit(false);
//			
//			SimpleJdbcCall jdbcCall =  new SimpleJdbcCall(ds)
//											.withProcedureName(SP_NAME)
//											.declareParameters(
//								                    new SqlParameter("P_MSG_ID", Types.VARCHAR),
//								                    new SqlParameter("V_USER_ID", Types.VARCHAR),
//								                    new SqlParameter("V_CHNL_ID", Types.VARCHAR),
//								                    new SqlParameter("V_ACCT_ST_SLC", Types.VARCHAR),
//								                    new SqlParameter("V_IN_ACCT_NO", Types.VARCHAR),
//								                    new SqlParameter("P_FUNC_CODE", Types.VARCHAR),
//								                    new SqlParameter("P_ACCT_LIST_CD", Types.VARCHAR),
//								                    new SqlParameter("P_ACCT_CHANNEL_LINK", Types.VARCHAR),
//								                    new SqlParameter("P_ACCT_TYPE_SELECTOR", Types.VARCHAR),
//								                    new SqlParameter("P_ATM_CARD_LINK", Types.VARCHAR),
//								                    new SqlParameter("P_ATM_CARD_NUMBER", Types.VARCHAR),
//								                    new SqlOutParameter("RESPONSE_CODE", Types.VARCHAR),
//						                    		new SqlOutParameter("NUMBER_OF_ROWS", Types.VARCHAR),
//													new SqlOutParameter("P_RETURN_LIST", OracleTypes.CURSOR, new AccountMapper()));
//			
//			MapSqlParameterSource paramMap = new MapSqlParameterSource()
//		              .addValue("P_MSG_ID", MSG_FORMAT)					// TODO: or use MSG_VERSION
//		              .addValue("V_USER_ID", userId)
//		              .addValue("V_CHNL_ID", CHANNEL_ID)
//		              .addValue("V_ACCT_ST_SLC", ACCT_STATUS_SELECTOR)
//		              .addValue("V_IN_ACCT_NO", accountNo)
//		              .addValue("P_FUNC_CODE", CHANNEL_FUNCTION)
//		              .addValue("P_ACCT_LIST_CD", ACCT_LIST_CODE)
//		              .addValue("P_ACCT_CHANNEL_LINK", ACCT_CHANNEL_LINK)
//		              .addValue("P_ACCT_TYPE_SELECTOR", ACCT_TYPE_SELECTOR)
//		              .addValue("P_ATM_CARD_LINK", ATM_CARD_LINK)
//		              .addValue("P_ATM_CARD_NUMBER", ATM_CARD_NUMBER);
//			
//			resultMap = jdbcCall.execute(paramMap);
//			
//			final String responseCode = (String) resultMap.get("RESPONSE_CODE");
//			final String numberOfRows = (String) resultMap.get("NUMBER_OF_ROWS");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//
//		}
//		return (List<Account>) resultMap.get("P_RETURN_LIST");
//	}


}
