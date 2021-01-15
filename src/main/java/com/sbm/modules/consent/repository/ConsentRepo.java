package com.sbm.modules.consent.repository;

import com.sbm.modules.consent.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbm.modules.consent.model.Consent;

import java.util.List;

@Repository
public interface ConsentRepo extends JpaRepository<Consent, Long> {

    public Consent getConsentByAccount_UserUserName(String userName);

    @Query("select cnsnt from Consent cnsnt where cnsnt.account.user.userName = :userName")
    public List<Consent> getConsentByUserName(@Param("userName") String userName);


    @Query("select cnsnt from Consent cnsnt where cnsnt.account.user.id = :userId")
    public List<Consent> getConsentByUserId(@Param("userId") Long userId);

    @Query("select cnsnt.app from Consent cnsnt where cnsnt.account.user.id = :userId")
    public List<App> getConsentAppsByUserId(@Param("userId") Long userId);

    public List<Consent> getConsentByAccount_AccountNumberAndApp_ClientId(@Param("accountNumber") String accountNumber, @Param("appId") String appId);
}
