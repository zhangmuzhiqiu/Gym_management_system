package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.MonthlySalary;
import com.example.Gym_management_system.entity.SalaryCustomer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
public interface ISalaryCustomerService extends IService<SalaryCustomer> {

    List<MonthlySalary> calculateMonthlyIncome();
}
