package com.sbm.modules.consent.service.consent;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.consts.ConsentStatus;
import com.sbm.modules.consent.dto.ConsentDto;
import com.sbm.modules.consent.dto.AppDto;
import com.sbm.modules.consent.model.Account;
import com.sbm.modules.consent.model.App;
import com.sbm.modules.consent.model.Consent;
import com.sbm.modules.consent.model.Permission;
import com.sbm.modules.consent.repository.ConsentRepo;
import com.sbm.modules.consent.service.app.AppService;
import com.sbm.modules.consent.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsentServiceImpl implements ConsentService {


    @Autowired
    private ConsentRepo consentRepo;

    @Autowired
    private MapperHelper mapperHelper;

    @Autowired
    private AppService appService;

    @Autowired
    private PermissionService permissionService;

    private String approvalParameter = OAuth2Utils.USER_OAUTH_APPROVAL;

    @Override
    public List<ConsentDto> getConsentByUserName(String userName) {
        List<Consent> consent = consentRepo.getConsentByUserName(userName);
        return mapperHelper.transform(consent, ConsentDto.class);
    }

    @Override
    public List<ConsentDto> getConsentByUserId(Long userId) {
        List<Consent> consent = consentRepo.getConsentByUserId(userId);
        return mapperHelper.transform(consent, ConsentDto.class);
    }

    @Override
    public List<AppDto> getConsentAppsForUser(Long userId) {
        List<App> thirdParties = consentRepo.getConsentAppsByUserId(userId);
        return mapperHelper.transform(thirdParties, AppDto.class);
    }

    @Override
    public void save(AuthorizationRequest authorizationRequest) {
        Consent consent = new Consent();
        Account acc = new Account();
        App app = appService.getAppByClientId(authorizationRequest.getClientId());
        Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();

        acc.setId(1L);
        consent.setAccount(acc);
        consent.setApp(app);

        List<Permission> perms = new ArrayList<>();

        addPermissionToConsent(approvalParameters, perms);
        consent.setPermissions(perms);
         if(perms != null && perms.size() > 0) {
             consent.setStatus(ConsentStatus.AUTHORIZED.name());
         } else {
             consent.setStatus(ConsentStatus.REJECTED.name());
         }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        consent.setExpirationDateTime(cal.getTime());
        consent.setTransactionFromDateTime(new Date());
        consent.setTransactionToDateTime(cal.getTime());
        consentRepo.save(consent);
    }

    private void addPermissionToConsent(Map<String, String> approvalParameters, List<Permission> perms) {
        approvalParameters.forEach((k, v) -> {
            if(!approvalParameter.equals(k) && !"authorize".equals(k)) {
                if("true".equals(v)) {
                    Permission perm = permissionService.getPermissionByCode(k);
                    if (perm != null) {
                        perms.add(perm);
                    }
                }
            }
        });
    }
}
