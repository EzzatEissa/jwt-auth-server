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
import com.sbm.modules.consent.service.account.AccountsService;
import com.sbm.modules.consent.service.app.AppService;
import com.sbm.modules.consent.service.permission.PermissionService;
import com.sbm.modules.openbanking.service.AccountService;
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

    @Autowired
    AccountsService accountService;

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
    public void save(AuthorizationRequest authorizationRequest,  List<String> accounts) {

        App app = appService.getAppByClientId(authorizationRequest.getClientId());
        Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();

        if(accounts != null && !accounts.isEmpty()) {
            accounts.stream().forEach(accountNumber -> {
                Consent consent = new Consent();
                Account account = accountService.getAccount(accountNumber.trim());
                if(account != null) {

                    List<Consent> consents = getConsentByAccountAndApp(accountNumber.trim(), app.getClientId());
                    if(consents != null && !consents.isEmpty()) {
                        consents.stream().forEach(cnsnt -> {
                            consentRepo.delete(cnsnt);
                        });
                    }
                    consent.setAccount(account);
                    consent.setApp(app);
                    if (authorizationRequest.getScope() != null && !authorizationRequest.getScope().isEmpty()) {
                        List<Permission> perms = new ArrayList<>();

                        addPermissionToConsent(authorizationRequest.getScope(), perms);
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

                }

            });
        }

    }

    private void addPermissionToConsent(Set<String> scopesStr, List<Permission> perms) {
        scopesStr.stream().forEach(permissionStr -> {
            Permission perm = permissionService.getPermissionByCode(permissionStr);
            if (perm != null) {
                perms.add(perm);
            }
        });
    }

    @Override
    public List<Consent> getConsentByAccountAndApp(String accountNumber, String appId) {
        return consentRepo.getConsentByAccount_AccountNumberAndApp_ClientId(accountNumber, appId);
    }
}
