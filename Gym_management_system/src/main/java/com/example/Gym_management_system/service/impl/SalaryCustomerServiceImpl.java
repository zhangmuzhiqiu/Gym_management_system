package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.entity.*;
import com.example.Gym_management_system.mapper.CourseMapper;
import com.example.Gym_management_system.mapper.CourseRecordingMapper;
import com.example.Gym_management_system.mapper.SalaryCustomerMapper;
import com.example.Gym_management_system.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SalaryCustomerServiceImpl extends ServiceImpl<SalaryCustomerMapper, SalaryCustomer> implements ISalaryCustomerService {

    @Autowired
    ICustomerService customerService;
    @Autowired
    ITimesPriceService timesPriceService;
    @Autowired
    SalaryCustomerMapper salaryCustomerMapper;
    @Autowired
    ICourseCustomerService courseCustomerService;
    @Autowired
    CourseRecordingMapper courseRecordingMapper;
    @Autowired
    CourseMapper courseMapper;

    public List<MonthlySalary> calculateMonthlyIncome() {
        // 获取所有客户
        List<Customer> customers = customerService.list();

        // 获取所有的价格记录
        List<TimesPrice> prices = timesPriceService.list();

        // 创建一个TreeMap来存储每个月的总收入
        Map<YearMonth, BigDecimal> incomeMap = new TreeMap<>();

        YearMonth currentMonth = YearMonth.now();

        for (Customer customer : customers) {
            // 获取此客户的所有工资记录
            List<SalaryCustomer> salaries = salaryCustomerMapper.selectList(
                    new QueryWrapper<SalaryCustomer>()
                            .eq("employee_id", customer.getId())
                            .eq("transaction_type", "办卡")
            );

            for (SalaryCustomer salary : salaries) {
                YearMonth salaryMonth = YearMonth.from(salary.getTransactionDate());

                if (salaryMonth.isAfter(currentMonth)) {
                    continue;
                }

                // 把这个月的收入加到总收入中
                incomeMap.put(salaryMonth, incomeMap.getOrDefault(salaryMonth, BigDecimal.ZERO).add(salary.getAmount()));
            }
        }

        List<MonthlySalary> monthlyIncomes = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal> entry : incomeMap.entrySet()) {
            monthlyIncomes.add(new MonthlySalary(entry.getKey(), entry.getValue()));
        }

        for (MonthlySalary mi : monthlyIncomes) {
            System.out.println(mi);
        }
        checkAndAddSalaryRecord();
        checkAndAddCoursePaymentRecords();
        return monthlyIncomes;
    }


    public void checkAndAddSalaryRecord() {
        // 获取当前日期
        LocalDate now = LocalDate.now();

        // 获取所有客户
        List<Customer> customers = customerService.list();

        // 获取所有的价格记录
        List<TimesPrice> prices = timesPriceService.list();

        for (Customer customer : customers) {
            // 获取此客户的所有工资记录
            List<SalaryCustomer> salaries = salaryCustomerMapper.selectList(
                    new QueryWrapper<SalaryCustomer>()
                            .eq("employee_id", customer.getId())
                            .eq("transaction_type", "办卡")
            );

            // 如果没有办卡记录，则添加一条新的工资记录
            if (salaries.isEmpty()) {
                // 根据用户的次数，找到对应的价格
                // 如果用户的次数不在价格表中，向上取更高次数的价格
                BigDecimal price = findPriceForTimes(prices, customer.getTimes());

                SalaryCustomer newSalary = new SalaryCustomer();
                newSalary.setEmployeeId(customer.getId());
                newSalary.setTransactionDate(customer.getCreateTime());
                newSalary.setTransactionType("办卡");
                newSalary.setAmount(price);
                newSalary.setDescription("办卡收入");
                newSalary.setStatus("待处理");
                newSalary.setCreatedAt(LocalDateTime.now());
                newSalary.setUpdatedAt(LocalDateTime.now());

                salaryCustomerMapper.insert(newSalary);
            }
        }
    }

    private BigDecimal findPriceForTimes(List<TimesPrice> prices, int times) {
        // Sort the prices by times in ascending order
        prices.sort(Comparator.comparing(TimesPrice::getTimes));

        // Find the price for the given times
        for (TimesPrice price : prices) {
            if (price.getTimes() >= times) {
                return price.getPrice();
            }
        }

        // If the times is greater than the maximum times in the price list,
        // return the price for the maximum times
        return prices.get(prices.size() - 1).getPrice();
    }

    public void checkAndAddCoursePaymentRecords() {
        // 获取所有CourseCustomer记录
        List<CourseCustomer> courseCustomers = courseCustomerService.list();

        for (CourseCustomer courseCustomer : courseCustomers) {
            // 获取此客户的所有缴费记录
            List<SalaryCustomer> payments = salaryCustomerMapper.selectList(
                    new QueryWrapper<SalaryCustomer>()
                            .eq("employee_id", courseCustomer.getCustomerId())
                            .eq("transaction_type", "缴费")
            );

            // 如果没有缴费记录，则添加一条新的缴费记录
            if (payments.isEmpty()) {
                // 获取此客户的所有CourseRecording记录
                List<CourseRecording> recordings = courseRecordingMapper.selectList(
                        new QueryWrapper<CourseRecording>()
                                .eq("id", courseCustomer.getRecordingId())
                );

                // 获取此CourseRecording对应的Course
                Course course = courseMapper.selectById(recordings.get(0).getCourseId());

                // 创建新的SalaryCustomer记录
                SalaryCustomer newSalary = new SalaryCustomer();
                newSalary.setEmployeeId(courseCustomer.getCustomerId());
                newSalary.setTransactionDate(LocalDate.now());
                newSalary.setTransactionType("缴费");
                newSalary.setAmount(course.getCoursePrice());
                newSalary.setDescription("课程缴费");
                newSalary.setStatus("待处理");
                newSalary.setCreatedAt(LocalDateTime.now());
                newSalary.setUpdatedAt(LocalDateTime.now());

                salaryCustomerMapper.insert(newSalary);
            }
        }
    }

}
