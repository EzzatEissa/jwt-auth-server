package com.sbm.modules.consent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbm.modules.consent.model.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    public Account getAccountByAccountNumber(String accountNumber);

    public List<Account> getAccountByUser_Id(Long userId);

}
