package com.sbm.modules.consent.service.consent;

import com.sbm.modules.consent.dto.ConsentDto;
import com.sbm.modules.consent.dto.AppDto;
import com.sbm.modules.consent.model.Consent;
import org.springframework.security.oauth2.provider.AuthorizationRequest;

import java.util.List;

public interface ConsentService {


    public List<ConsentDto> getConsentByUserName(String userName);

    public List<ConsentDto> getConsentByUserId(Long userId);

    public List<AppDto> getConsentAppsForUser(Long userId);

    public void save(AuthorizationRequest authorizationRequest);
}
