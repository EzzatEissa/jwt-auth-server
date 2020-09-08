package com.sbm.modules.consent.service.permission;

import com.sbm.common.utils.MapperHelper;
import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.dto.PermissionForm;
import com.sbm.modules.consent.model.Permission;
import com.sbm.modules.consent.repository.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;


    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public boolean savePermission(PermissionForm permission) throws Exception{
         try{
             permissionRepo.save(mapperHelper.transform(permission, Permission.class));
         }catch (Exception ex) {
             throw ex;
         }
        return true;
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        List<Permission> permissions= permissionRepo.findAll();
        return mapperHelper.transform(permissions, PermissionDto.class);
    }

    @Override
    public Permission getPermissionByCode(String code) {
        return permissionRepo.getPermissionByCode(code);
    }
}
