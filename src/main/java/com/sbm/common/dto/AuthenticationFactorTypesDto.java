package com.sbm.common.dto;

import lombok.Data;

@Data
public class AuthenticationFactorTypesDto {

    private String name;
    private String code;
    private Boolean enabled;
}
