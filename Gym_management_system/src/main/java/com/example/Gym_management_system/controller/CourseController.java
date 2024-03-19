package com.example.Gym_management_system.controller;

import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Course;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.service.ICourseService;
import com.example.Gym_management_system.service.ITokenService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@RequestMapping("/Gym_management_system/course")
public class CourseController {
/*
    添加课程  （添加课程后通过申请时间和场地对记录表操作）
    修改课程 {课程时间【时间等分，重复时间相互调换】、
                课程场地【替换场地时间相同相互调换、公共场地没有课程】、
                名称、价格、用户【添加用户、删除用户】、是否完成
    todo 查询课程 {分天查询【时间排序】、教练查询、id查询、场地查询}
    todo 删除课程
*/

    @Autowired
    ICourseService courseService;
    @Autowired
    ITokenService tokenService;


    @PostMapping("addCourse")
    public R addCourse(@RequestBody Course course,HttpServletRequest request) {
        log.info("打印addCourse:{}", course);
        course.setDel(false);
        Trainer trainer = tokenService.getTrainerOfToken(request);
        course.setTrainerId(trainer.getId());
        courseService.save(course);
        return R.ok();
    }

    @PostMapping("updateCourse")
    public R updateCourse(@RequestBody Course course) {
        log.info("打印updateCourse:{}", course);
        courseService.updateById(course);
        return R.ok();
    }

    @GetMapping("selCourseByTrainer")
    public R selCourseByTrainer(@RequestParam("pageIndex") Integer pageIndex,
                                @RequestParam("courseName") String courseName,
                                HttpServletRequest request) {
        log.info("打印selCourseByTrainer:{},{}", courseName, pageIndex);
        PageInfo pageInfo = courseService.selCourseByTrainer(courseName, pageIndex, request);
        return R.ok().data("pageInfo", pageInfo);
    }

    @GetMapping("selCourseById/{id}")
    public R selCourseById(@PathVariable Integer id) {
        log.info("打印selCourseById:{}", id);
        Optional<Course> optById = courseService.getOptById(id);
        return R.ok().data("course", optById.get());
    }

    @GetMapping("delCourseById/{id}")
    public R delCourseById(@PathVariable Integer id,HttpServletRequest request) {
        log.info("打印delCourseById:{}", id);
        if (tokenService.getTokenOkByTrainer(request)){
            return R.ok("权限不足，不能执行删除操作");
        }
        Optional<Course> optById = courseService.getOptById(id);
        optById.get().setDel(true);
        courseService.updateById(optById.get());
        return R.ok();
    }

    @GetMapping("getAllCourse")
    public R getAllCourse() {
        List<Course> list = courseService.list();
        ArrayList<Course> courses = new ArrayList<>();
        for (Course course : list) {
            Course course1 = new Course();
            course1.setCourseName(course.getCourseName());
            course1.setId(course.getId());
            courses.add(course1);
        }
        return R.ok().data("pageInfo", courses);
    }



}
