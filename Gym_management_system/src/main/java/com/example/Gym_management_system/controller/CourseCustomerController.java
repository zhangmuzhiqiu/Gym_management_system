package com.example.Gym_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Course;
import com.example.Gym_management_system.entity.CourseCustomer;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.mapper.CustomerMapper;
import com.example.Gym_management_system.service.ICourseCustomerService;
import com.example.Gym_management_system.service.ICourseService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
@RestController
@Slf4j
@RequestMapping("/Gym_management_system/courseCustomer")
public class CourseCustomerController {

    @Autowired
    ICourseCustomerService courseCustomerService;
    @Autowired
    CustomerMapper customerMapper;


    @GetMapping("addCourseCustomer/{id}")
    public R addCourseCustomer(@PathVariable Integer id, HttpServletRequest request) {
        log.info("打印addCourseCustomer:{}", id);
        CourseCustomer courseCustomer = new CourseCustomer();
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("username",jwtUser.getUsername());
        wrapper.eq("password",jwtUser.getPassword());
        Customer customer = customerMapper.selectOne(wrapper);
        courseCustomer.setCustomerId(customer.getId());
        courseCustomer.setDel(false);
        courseCustomer.setRecordingId(id);
        courseCustomerService.save(courseCustomer);
        return R.ok();
    }

    @GetMapping("delCourseCustomer/{id}")
    public R delCourseCustomer(@PathVariable Integer id) {
        log.info("打印delCourseCustomer:{}", id);
        courseCustomerService.removeById(id);
        return R.ok();
    }
}
