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
public class SelTrainerVo {

    private Integer pageIndex;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("入职时间")
    private LocalDate create_time;

    @ApiModelProperty("工作类型")
    private String work;

}
