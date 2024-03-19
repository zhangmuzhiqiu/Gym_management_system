package com.example.Gym_management_system.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JWTLoginEntity {

    private String username;
    private String password;
    @ApiModelProperty("工作类型")
    private Integer roles;
}
