package com.example.Gym_management_system.controller;

import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.MonthlySalary;
import com.example.Gym_management_system.pojo.vo.salary.SelSalary;
import com.example.Gym_management_system.service.ISalaryCustomerService;
import com.example.Gym_management_system.service.ISalaryTrainerService;
import com.example.Gym_management_system.service.ISalaryWorkerService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
@RestController
@Slf4j
@RequestMapping("/Gym_management_system/salaryWorker")
public class SalaryWorkerController {
// todo 计算每月总收入、支出
//    计算 所有个人工资
//    计算用户缴费
//    每月工资表
    @Autowired
    ISalaryWorkerService salaryWorkerService;

    @Autowired
    ISalaryTrainerService salaryTrainerService;

    @Autowired
    ISalaryCustomerService salaryCustomerService;

//    总收入
    @GetMapping("revenueAll")
    public R revenueAll() {
        List<MonthlySalary> monthlySalaries = salaryCustomerService.calculateMonthlyIncome();

        return R.ok().data("monthlySalaries", monthlySalaries);
    }

//    总支出
    @GetMapping("expendituresAll")
    public R expendituresAll() {
        List<MonthlySalary> monies = salaryTrainerService.expenditures();
        List<MonthlySalary> monies1 = salaryWorkerService.expenditures();
        Map<YearMonth, BigDecimal> salaryMap = new HashMap<>();

        for (MonthlySalary ms : monies) {
            salaryMap.put(ms.getMonth(), salaryMap.getOrDefault(ms.getMonth(), BigDecimal.ZERO).add(ms.getTotalSalary()));
        }

        for (MonthlySalary ms : monies1) {
            salaryMap.put(ms.getMonth(), salaryMap.getOrDefault(ms.getMonth(), BigDecimal.ZERO).add(ms.getTotalSalary()));
        }

        List<MonthlySalary> mergedSalaries = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal> entry : salaryMap.entrySet()) {
            mergedSalaries.add(new MonthlySalary(entry.getKey(), entry.getValue()));
        }

        mergedSalaries.sort(Comparator.comparing(ms -> ms.getMonth()));

        return R.ok().data("mergedSalaries",mergedSalaries);
    }

    @PostMapping("selSalaryList")
    public R selSalaryList(@RequestBody SelSalary selSalary){
        PageInfo pageInfo = salaryWorkerService.selSalaryList(selSalary);
        return R.ok().data("pageInfo",pageInfo);
    }
}
