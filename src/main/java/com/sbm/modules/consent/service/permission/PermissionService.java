package com.sbm.modules.consent.service.permission;

import java.util.List;

import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.dto.PermissionForm;
import com.sbm.modules.consent.model.Permission;

public interface PermissionService {

	public boolean savePermission(PermissionForm permission) throws Exception;

	public List<PermissionDto> getAllPermissions();

	public Permission getPermissionByCode(String code);

	public List<PermissionDto> getPermissionsByCodes(List<String> codes);
}
