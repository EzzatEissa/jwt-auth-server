package com.sbm.modules.consent.service.permission;

import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.dto.PermissionForm;
import com.sbm.modules.consent.model.Permission;

import java.util.List;

public interface PermissionService {

    public boolean savePermission(PermissionForm permission) throws Exception;

    public List<PermissionDto> getAllPermissions();

    public Permission getPermissionByCode(String code);
}
