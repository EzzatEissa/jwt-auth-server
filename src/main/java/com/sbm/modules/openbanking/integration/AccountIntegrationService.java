package com.sbm.modules.openbanking.integration;


import com.sbm.modules.openbanking.model.Account;

import java.util.List;

public interface AccountIntegrationService {

	public List<Account> getAccounts(String userId, String accountNo);
}
