package com.sbm.modules.consent.repository;

import com.sbm.modules.consent.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepo extends JpaRepository<App, Long> {

    public App getAppByClientId(String clientId);
}
