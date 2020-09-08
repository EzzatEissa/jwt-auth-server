package com.sbm.modules.openbanking.service;

import com.sbm.modules.openbanking.model.Account;

import java.util.List;

public interface AccountService {

	public List<Account> getAccounts(String userId, String accountNo);
}
