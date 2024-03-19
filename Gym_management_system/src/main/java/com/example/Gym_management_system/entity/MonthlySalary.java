package com.example.Gym_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySalary {

    YearMonth month;
    BigDecimal totalSalary;
}
