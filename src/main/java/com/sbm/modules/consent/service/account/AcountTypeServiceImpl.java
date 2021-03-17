package com.sbm.modules.consent.service.account;

import com.sbm.modules.consent.model.AccountType;
import com.sbm.modules.consent.repository.AccountTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcountTypeServiceImpl implements AccountTypeService{

	@Autowired
	private AccountTypeRepo accountTypeRepo;
	@Override
	public AccountType getAccountTypeByCode(String accountType) {
		return accountTypeRepo.getAccountTypeByCode(accountType);
	}
}
