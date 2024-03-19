package com.example.Gym_management_system.pojo.vo.salary;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryListVo {

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
}
