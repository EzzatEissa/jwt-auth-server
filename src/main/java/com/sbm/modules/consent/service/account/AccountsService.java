package com.sbm.modules.consent.service.account;

import java.util.List;
import java.util.Set;

import com.sbm.modules.consent.dto.AccountDto;
import com.sbm.modules.consent.model.Account;

public interface AccountsService {

    public Account saveAccount(Account account);

    public Set<Account> getAllAccounts();

    public Account getAccount(String accountNumber);

    public List<AccountDto> getAccountsByUserId(Long userId);
}
