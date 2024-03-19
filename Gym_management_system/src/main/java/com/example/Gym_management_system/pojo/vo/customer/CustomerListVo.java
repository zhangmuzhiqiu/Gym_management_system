package com.example.Gym_management_system.pojo.vo.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListVo {

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

    @ApiModelProperty("创建时间")
    private LocalDate createTime;

    @ApiModelProperty("会员到期时间")
    private LocalDate expiryTime;

    @ApiModelProperty("可使用次数")
    private Integer times;

    @ApiModelProperty("会员是否在店")
    private String inGym;
}
