package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
public interface ICourseService extends IService<Course> {


    PageInfo selCourseByTrainer(String courseName, Integer pageIndex, HttpServletRequest request);
}
