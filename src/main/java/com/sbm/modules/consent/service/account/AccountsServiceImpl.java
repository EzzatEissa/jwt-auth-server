package com.sbm.modules.consent.service.account;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.AccountDto;
import com.sbm.modules.consent.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbm.modules.consent.model.Account;
import com.sbm.modules.consent.repository.AccountRepo;

@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public Account saveAccount(Account account) {
        return accountRepo.save(account);
    }

    @Override
    public Set<Account> getAllAccounts() {
        return new HashSet<>(accountRepo.findAll());
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accountRepo.getAccountByAccountNumber(accountNumber);
    }


    @Override
    public List<AccountDto> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepo.getAccountByUser_Id(userId);

        return mapperHelper.transform(accounts, AccountDto.class);
    }
}
