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
public class SelCustomerVo {

    private Integer pageIndex;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("创建时间")
    private LocalDate create_time;

    @ApiModelProperty("可使用次数")
    private Integer times;

    @ApiModelProperty("会员是否在店")
    private Boolean in_gym;
}
