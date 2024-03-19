package com.example.Gym_management_system.pojo.vo.worker;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelWorkerVo {

    private Integer pageIndex;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("工作类型")
    private Integer roles;

    private LocalDate create_time;
}
