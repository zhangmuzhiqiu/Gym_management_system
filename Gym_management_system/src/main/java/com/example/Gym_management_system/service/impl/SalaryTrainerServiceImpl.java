package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.entity.*;
import com.example.Gym_management_system.mapper.SalaryTrainerMapper;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.mapper.WorkerMapper;
import com.example.Gym_management_system.service.ISalaryTrainerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.service.ITrainerService;
import com.example.Gym_management_system.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
@Service
public class SalaryTrainerServiceImpl extends ServiceImpl<SalaryTrainerMapper, SalaryTrainer> implements ISalaryTrainerService {

    @Autowired
    TrainerMapper trainerMapper;
    @Autowired
    ITrainerService trainerService;
    @Autowired
    SalaryTrainerMapper salaryTrainerMapper;

    @Override
    public List<MonthlySalary> expenditures() {
        List<Trainer> list = trainerService.list();
        List<Employee> employees = new ArrayList<>();

        for (Trainer trainer : list) {
            Employee employee = new Employee();
            employee.setSalary(trainer.getSalary());
            employee.setJoinDate(trainer.getCreateTime().atStartOfDay());
            if (!Objects.isNull(trainer.getExpiryTime())){
                employee.setResignDate(trainer.getExpiryTime().atStartOfDay());
            }
            employees.add(employee);
        }

        Map<YearMonth, BigDecimal> salaryMap = new TreeMap<>();

        YearMonth currentMonth = YearMonth.now();

        for (Employee e : employees) {
            YearMonth start = YearMonth.from(e.getJoinDate());
            YearMonth end = e.getResignDate() != null ? YearMonth.from(e.getResignDate()) : currentMonth;
            end = end.isAfter(currentMonth) ? currentMonth : end;
            for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
                salaryMap.put(ym, salaryMap.getOrDefault(ym, BigDecimal.ZERO).add(e.getSalary()));
            }
        }

        List<MonthlySalary> monthlySalaries = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal> entry : salaryMap.entrySet()) {
            monthlySalaries.add(new MonthlySalary(entry.getKey(), entry.getValue()));
        }

        for (MonthlySalary ms : monthlySalaries) {
            System.out.println(ms);
        }
        checkAndAddSalaryRecords();
        return monthlySalaries;
    }

    public void checkAndAddSalaryRecords() {
        // 获取当前月份
        LocalDate now = LocalDate.now();

        // 获取所有教练
        List<Trainer> trainers = trainerService.list();

        for (Trainer trainer : trainers) {
            // 获取此教练的所有工资记录
            List<SalaryTrainer> salaries = salaryTrainerMapper.selectList(
                    new QueryWrapper<SalaryTrainer>()
                            .eq("employee_id", trainer.getId())
                            .eq("transaction_type", "工资")
            );

            // 获取教练入职日期
            LocalDate startDate = trainer.getCreateTime();

            // 如果教练已经离职，获取离职日期，否则假设离职日期是当前日期
            LocalDate endDate = trainer.getExpiryTime() != null ? trainer.getExpiryTime() : now;

            // 遍历从教练入职日期到离职日期的每个月份
            for (LocalDate date = startDate; !date.isAfter(endDate) && !date.isAfter(now); date = date.plusMonths(1)) {
                int year = date.getYear();
                int month = date.getMonthValue();

                // 检查是否存在这个月份的工资记录
                boolean recordExists = salaries.stream().anyMatch(salary -> {
                    LocalDate salaryDate = salary.getTransactionDate();
                    return salaryDate.getYear() == year && salaryDate.getMonthValue() == month;
                });

                // 如果不存在这个月份的工资记录，则添加一条新的工资记录
                if (!recordExists) {
                    SalaryTrainer newSalary = new SalaryTrainer();
                    newSalary.setEmployeeId(trainer.getId());
                    newSalary.setTransactionDate(date);
                    newSalary.setTransactionType("工资");
                    newSalary.setAmount(trainer.getSalary());
                    newSalary.setDescription("月工资");
                    newSalary.setStatus("待处理");
                    newSalary.setCreatedAt(LocalDateTime.now());
                    newSalary.setUpdatedAt(LocalDateTime.now());

                    salaryTrainerMapper.insert(newSalary);
                }
            }
        }
    }



}
