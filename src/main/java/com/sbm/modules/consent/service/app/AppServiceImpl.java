package com.sbm.modules.consent.service.app;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.AppDto;
import com.sbm.modules.consent.model.App;
import com.sbm.modules.consent.repository.AppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepo appRepo;

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public List<AppDto> getAllApps() {
        List<App> thirdParties = appRepo.findAll();
        return mapperHelper.transform(thirdParties, AppDto.class);
    }

    @Override
    public App getAppByClientId(String clientId) {
        return appRepo.getAppByClientId(clientId);
    }
}
