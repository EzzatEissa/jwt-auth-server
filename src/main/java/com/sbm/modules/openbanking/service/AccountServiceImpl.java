package com.sbm.modules.openbanking.service;

import com.sbm.modules.openbanking.integration.AccountIntegrationService;
import com.sbm.modules.openbanking.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountIntegrationService integrationService;
	
	public List<Account> getAccounts(String userId, String accountNo) {

		return integrationService.getAccounts(userId, accountNo);
	}
}
