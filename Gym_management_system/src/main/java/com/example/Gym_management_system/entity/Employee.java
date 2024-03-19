package com.example.Gym_management_system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    LocalDateTime joinDate;
    LocalDateTime resignDate;
    BigDecimal salary;

}
