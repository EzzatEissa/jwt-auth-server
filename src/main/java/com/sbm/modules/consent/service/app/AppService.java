package com.sbm.modules.consent.service.app;

import com.sbm.modules.consent.dto.AppDto;
import com.sbm.modules.consent.model.App;

import java.util.List;

public interface AppService {

    public List<AppDto> getAllApps();

    public App getAppByClientId(String clientId);
}
