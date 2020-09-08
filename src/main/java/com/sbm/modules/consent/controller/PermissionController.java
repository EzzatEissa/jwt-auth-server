package com.sbm.modules.consent.controller;

import com.sbm.modules.consent.dto.PermissionDto;
import com.sbm.modules.consent.dto.PermissionForm;
import com.sbm.modules.consent.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/admin/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = "/all", method = GET, headers = "Accept=application/json")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissionDtos = permissionService.getAllPermissions();
        return new ResponseEntity<>(permissionDtos, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> savePermission(@RequestBody @Valid PermissionForm permissionForm) throws Exception{
        permissionService.savePermission(permissionForm);
        return new ResponseEntity<>("SAVED", HttpStatus.OK);

    }

}
