package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.entity.*;
import com.example.Gym_management_system.mapper.SalaryCustomerMapper;
import com.example.Gym_management_system.mapper.SalaryTrainerMapper;
import com.example.Gym_management_system.mapper.SalaryWorkerMapper;
import com.example.Gym_management_system.mapper.WorkerMapper;
import com.example.Gym_management_system.pojo.mapstruct.TrainerMapstruct;
import com.example.Gym_management_system.pojo.vo.salary.SalaryListVo;
import com.example.Gym_management_system.pojo.vo.salary.SelSalary;
import com.example.Gym_management_system.pojo.vo.trainer.TrainerListVo;
import com.example.Gym_management_system.service.ISalaryWorkerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.service.IWorkerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-02-19
 */
@Service
@Slf4j
public class SalaryWorkerServiceImpl extends ServiceImpl<SalaryWorkerMapper, SalaryWorker> implements ISalaryWorkerService {

    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    IWorkerService workerService;
    @Autowired
    SalaryWorkerMapper salaryWorkerMapper;
    @Autowired
    SalaryCustomerMapper salaryCustomerMapper;
    @Autowired
    SalaryTrainerMapper salaryTrainerMapper;

    @Override
    public List<MonthlySalary> expenditures() {
        List<Worker> list = workerService.list();
        List<Employee> employees = new ArrayList<>();

        for (Worker worker : list) {
            Employee employee = new Employee();
            employee.setSalary(worker.getSalary());
            employee.setJoinDate(worker.getCreateTime().atStartOfDay());
            if (!Objects.isNull(worker.getLeaveTime())) {
                employee.setResignDate(worker.getLeaveTime().atStartOfDay());
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

    @Override
    public PageInfo selSalaryList(SelSalary selSalary) {
        if (Objects.isNull(selSalary.getPageIndex())) {
            selSalary.setPageIndex(1);
        }
        PageHelper.startPage(selSalary.getPageIndex(), 5);

//        遍历查询条件中不为空的值
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(selSalary)) {
            for (Field field : selSalary.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!Objects.isNull(field.get(selSalary)) & !field.getName().equals("pageIndex") & field.get(selSalary) != "") {
                        if (field.getName().equals("transaction_date")) {
                            wrapper.gt(field.getName(), field.get(selSalary));
                        } else if (field.getName().equals("transaction_type") || field.getName().equals("description")) {
                            wrapper.like(field.getName(), field.get(selSalary));
                        } else {
                            wrapper.eq(field.getName(), field.get(selSalary));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        List list = salaryWorkerMapper.selectList(wrapper);
        List list1 = salaryCustomerMapper.selectList(wrapper);
        List list2 = salaryTrainerMapper.selectList(wrapper);
        ArrayList arrayList = new ArrayList<>();
//        防止查询结果为空
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
        }else {
            for (Object o : list) {
             arrayList.add(o);
            }
        }
        if (Objects.isNull(list1)) {
            list1 = new ArrayList<>();
        }else {
            for (Object o : list1) {
                arrayList.add(o);
            }
        }
        if (Objects.isNull(list2)) {
            list2 = new ArrayList<>();
        }else {
            for (Object o : list2) {
                arrayList.add(o);
            }
        }
//        先将total赋值
        PageInfo pageInfo = new PageInfo<>(arrayList);
//        再将查询结果返回
        pageInfo.setList(arrayList);
        return pageInfo;
    }

    public void checkAndAddSalaryRecords() {
        // 获取当前月份
        LocalDate now = LocalDate.now();

        // 获取所有员工
        List<Worker> workers = workerService.list();

        for (Worker worker : workers) {
            // 获取此员工的所有工资记录
            List<SalaryWorker> salaries = salaryWorkerMapper.selectList(
                    new QueryWrapper<SalaryWorker>()
                            .eq("employee_id", worker.getId())
                            .eq("transaction_type", "工资")
            );

            // 获取员工入职日期
            LocalDate startDate = worker.getCreateTime();

            // 如果员工已经离职，获取离职日期，否则假设离职日期是当前日期
            LocalDate endDate = worker.getLeaveTime() != null ? worker.getLeaveTime() : now;

            // 遍历从员工入职日期到离职日期的每个月份
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
                    SalaryWorker newSalary = new SalaryWorker();
                    newSalary.setEmployeeId(worker.getId());
                    newSalary.setTransactionDate(date);
                    newSalary.setTransactionType("工资");
                    newSalary.setAmount(worker.getSalary());
                    newSalary.setDescription("月工资");
                    newSalary.setStatus("待处理");
                    newSalary.setCreatedAt(LocalDateTime.now());
                    newSalary.setUpdatedAt(LocalDateTime.now());

                    salaryWorkerMapper.insert(newSalary);
                }
            }
        }
    }


}
