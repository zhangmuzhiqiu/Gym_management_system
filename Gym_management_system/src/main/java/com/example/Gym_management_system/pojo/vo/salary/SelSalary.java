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
public class SelSalary {

    private Integer pageIndex;

    @ApiModelProperty("交易日期")
    private LocalDate transaction_date;

    @ApiModelProperty("交易类型")
    private String transaction_type;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态")
    private String status;
}
