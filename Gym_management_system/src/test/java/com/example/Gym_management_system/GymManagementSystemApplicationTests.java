package com.example.Gym_management_system;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.entity.*;
import com.example.Gym_management_system.mapper.CourseMapper;
import com.example.Gym_management_system.mapper.WorkerMapper;
import com.example.Gym_management_system.pojo.mapstruct.CustomerMapstruct;
import com.example.Gym_management_system.pojo.mapstruct.WorkerMapstruct;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.CustomerListVo;
import com.example.Gym_management_system.pojo.vo.worker.WorkerListVo;
import com.example.Gym_management_system.service.*;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@SpringBootTest
@MapperScan("com.example.Gym_management_system.mapper")
@Slf4j
class GymManagementSystemApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    ICourseService courseService;
    @Autowired
    ISalaryTrainerService salaryTrainerService;
    @Autowired
    ISalaryWorkerService salaryWorkerService;
    @Autowired
    IRoleMenuService roleMenuService;
    @Autowired
    ISalaryCustomerService salaryCustomerService;
//	/**
//	 *附加条件构造器QueryWrapper常用方法 ---这几个肯定够用了
//	 */
//	 wrapper.eq("数据库字段名", "条件值"); //相当于where条件
//	 wrapper.between("数据库字段名", "区间一", "区间二");//相当于范围内使用的between
//	 wrapper.like("数据库字段名", "模糊查询的字符"); //模糊查询like
//	 wrapper.groupBy("数据库字段名");  //相当于group by分组
//	 wrapper.in("数据库字段名", "包括的值,分割"); //相当于in
//	 wrapper.orderByAsc("数据库字段名"); //升序
//	 wrapper.orderByDesc("数据库字段名");//降序
//	 wrapper.ge("数据库字段名", "要比较的值"); //大于等于
//	 wrapper.le("数据库字段名", "要比较的值"); //小于等于

    @Test
    void mappertest() {
        Worker worker = new Worker();
        worker.setUsername("admin");
        worker.setPassword("123456");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", worker.getUsername());
        wrapper.eq("password", worker.getPassword());
//		List<Worker> workers = workerMapper.selectList(null);
//		List<Worker> list = workerMapper.selectList(wrapper);
//		List list = workerMapper.selectMaps(wrapper);
        List<Worker> workers = workerMapper.selectList(null);
    }


    @Test
    void mapstructTest() {
        Worker worker = new Worker();
        worker.setUsername("admin");
        worker.setPassword("123456");
        JWTLoginEntity jwtLoginEntity = WorkerMapstruct.INSTANCE.dtoWorkerToJWT(worker);
        System.out.println(jwtLoginEntity);
    }

    @Test
    @Transactional
    void actionalTest() {
        Worker worker = new Worker();
        worker.setUsername("admin");
        worker.setPassword("123456");
        workerMapper.insert(worker);
        int a = 1 / 0;
        System.out.println(2);
    }

    @Test
    void mapstructnameTest() {
        List<Worker> workers = workerMapper.selectList(null);
        workers.forEach(worker -> {
            WorkerListVo workerListVo = WorkerMapstruct.INSTANCE.dtoWorkerToListVo(worker);
            System.out.println(workerListVo);
        });
    }

    @Test
    void getnametest() {
        System.out.println(String.valueOf(UUID.randomUUID()));
    }

    @Test
    void CustomerMapstructTest() {
        Customer customer = new Customer();
        AddCustomerVo addCustomerVo = new AddCustomerVo();
        customer.setUsername("admin");
        addCustomerVo.setNickname("123123");
        customer.setPassword("123456");
        CustomerListVo jwtLoginEntity = CustomerMapstruct.INSTANCE.dtoToListVo(customer);
        Customer customer1 = CustomerMapstruct.INSTANCE.addVoToDao(addCustomerVo);
        System.out.println(customer1);
        System.out.println(jwtLoginEntity);
    }

    @Test
    void CourseMapTest() {
//		AddCourseVo addCourseVo = new AddCourseVo("123", new BigDecimal("333.3"),3,3, LocalDateTime.now());
//		CourseRecording courseRecording = CourseMapstruct.INSTANCE.AddCourseVoToCourseRecording(addCourseVo);
//		Course course = CourseMapstruct.INSTANCE.AddCourseVoToCourse(addCourseVo);
//		courseMapper.addCourse(course);
//		log.info("打印courseRecording:{}",courseRecording);
//		log.info("打印course:{}", course);
        Optional<Course> optById = courseService.getOptById(1);
        log.info("courseService.getOptById(1):{}", optById.get());
    }

    @Test
    void menuTest() {
//        List<MonthlySalary> monies = salaryTrainerService.expenditures();
//        List<MonthlySalary> monies1 = salaryWorkerService.expenditures();
//        Map<YearMonth, BigDecimal> salaryMap = new HashMap<>();
//
//        // Add salaries from monies to salaryMap
//        for (MonthlySalary ms : monies) {
//            salaryMap.put(ms.getMonth(), salaryMap.getOrDefault(ms.getMonth(), BigDecimal.ZERO).add(ms.getTotalSalary()));
//        }
//
//        // Add salaries from monies1 to salaryMap
//        for (MonthlySalary ms : monies1) {
//            salaryMap.put(ms.getMonth(), salaryMap.getOrDefault(ms.getMonth(), BigDecimal.ZERO).add(ms.getTotalSalary()));
//        }
//
//        // Convert salaryMap to a list of MonthlySalary
//        List<MonthlySalary> mergedSalaries = new ArrayList<>();
//        for (Map.Entry<YearMonth, BigDecimal> entry : salaryMap.entrySet()) {
//            mergedSalaries.add(new MonthlySalary(entry.getKey(), entry.getValue()));
//        }
//
//        // Sort the list by month
//        mergedSalaries.sort(Comparator.comparing(ms -> ms.getMonth()));
//        System.out.println(mergedSalaries);
        List<MonthlySalary> monthlySalaries = salaryCustomerService.calculateMonthlyIncome();
        System.out.println(monthlySalaries);
    }
}
