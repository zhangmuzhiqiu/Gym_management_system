package com.example.Gym_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("s_salary_trainer")
@ApiModel(value = "SalaryTrainer对象", description = "")
public class SalaryTrainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工ID")
    private Integer employeeId;

    @ApiModelProperty("交易日期")
    private LocalDate transactionDate;

    @ApiModelProperty("交易类型")
    private String transactionType;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态")
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
