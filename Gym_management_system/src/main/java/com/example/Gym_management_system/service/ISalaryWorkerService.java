package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.MonthlySalary;
import com.example.Gym_management_system.entity.SalaryWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.pojo.vo.salary.SelSalary;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
public interface ISalaryWorkerService extends IService<SalaryWorker> {

    List<MonthlySalary> expenditures();

    PageInfo selSalaryList(SelSalary selSalary);
}
