package com.sbm.modules.consent.service.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.dto.PermissionForm;
import com.sbm.modules.consent.model.Permission;
import com.sbm.modules.consent.repository.PermissionRepo;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepo permissionRepo;

	@Autowired
	private MapperHelper mapperHelper;

	@Override
	public boolean savePermission(PermissionForm permission) throws Exception {
		try {
			this.permissionRepo.save(this.mapperHelper.transform(permission, Permission.class));
		}
		catch (Exception ex) {
			throw ex;
		}
		return true;
	}

	@Override
	public List<PermissionDto> getAllPermissions() {
		List<Permission> permissions = this.permissionRepo.findAll();
		return this.mapperHelper.transform(permissions, PermissionDto.class);
	}

	@Override
	public Permission getPermissionByCode(String code) {
		return this.permissionRepo.getPermissionByCode(code);
	}

	@Override
	public List<PermissionDto> getPermissionsByCodes(List<String> codes) {
		List<Permission> perms = this.permissionRepo.getPermissionByCodeIn(codes);
		return this.mapperHelper.transform(perms, PermissionDto.class);
	}

}
