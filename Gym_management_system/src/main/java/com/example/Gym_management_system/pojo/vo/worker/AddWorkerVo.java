package com.example.Gym_management_system.pojo.vo.worker;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWorkerVo {

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("工资")
    private BigDecimal salary;

    @ApiModelProperty("工作类型")
    private Integer roles;
}
