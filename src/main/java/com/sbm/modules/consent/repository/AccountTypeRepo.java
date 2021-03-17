package com.sbm.modules.consent.repository;

import com.sbm.modules.consent.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepo extends JpaRepository<AccountType, Long> {

	AccountType getAccountTypeByCode(String accountType);
}
