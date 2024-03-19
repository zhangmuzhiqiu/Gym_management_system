package com.example.Gym_management_system.pojo.vo.trainer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerListVo {
    private Integer id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("头像地址")
    private String avatarAddress;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("入职时间")
    private LocalDate createTime;

    @ApiModelProperty("离职时间")
    private LocalDate expiryTime;

    @ApiModelProperty("工资")
    private BigDecimal salary;

    @ApiModelProperty("工作类型")
    private String work;

}
