package com.example.Gym_management_system.pojo.vo.worker;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerListVo {
    private Integer id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("头像地址")
    private String avatarAddress;

    @ApiModelProperty("性别")
    private String sex;

    private LocalDate createTime;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("工资")
    private BigDecimal salary;

    @ApiModelProperty("工作类型")
    private Integer roles;
}
