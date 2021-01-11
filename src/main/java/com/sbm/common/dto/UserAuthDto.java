package com.sbm.common.dto;

import lombok.Data;

/**
 * Created by EzzatEissa on 1/7/2021.
 */
@Data
public class UserAuthDto {

    private Boolean successLogin;

    private Boolean secondFactorEnabled;

}
