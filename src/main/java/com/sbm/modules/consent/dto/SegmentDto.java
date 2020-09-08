package com.sbm.modules.consent.dto;

import com.sbm.common.dto.BaseDto;

import java.util.List;

public class SegmentDto extends BaseDto {

    private String name;
    private String code;

    private List<PermissionDto> permissions;

    private List<UserDto> user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }

    public List<UserDto> getUser() {
        return user;
    }

    public void setUser(List<UserDto> user) {
        this.user = user;
    }
}
