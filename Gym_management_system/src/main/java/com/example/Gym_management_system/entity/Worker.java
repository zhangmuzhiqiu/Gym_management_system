package com.example.Gym_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zzy
 * @since 2023-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_worker")
@ApiModel(value = "Worker对象", description = "")
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private Boolean sex;

    @ApiModelProperty("头像地址")
    private String avatarAddress;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("入职时间")
    private LocalDate createTime;

    @ApiModelProperty("工资")
    private BigDecimal salary;

    @ApiModelProperty("工作类型")
    private Integer roles;

    @ApiModelProperty("离职时间")
    private LocalDate leaveTime;
}
