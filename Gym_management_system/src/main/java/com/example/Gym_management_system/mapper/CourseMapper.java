package com.example.Gym_management_system.mapper;

import com.example.Gym_management_system.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
public interface CourseMapper extends BaseMapper<Course> {

    void addCourse(Course course);
}
