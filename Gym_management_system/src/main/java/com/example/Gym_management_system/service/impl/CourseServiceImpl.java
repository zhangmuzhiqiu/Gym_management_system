package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Course;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.CourseMapper;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
@Service //业务注解
@Transactional //事务注解
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    TrainerMapper trainerMapper;

    @Autowired
    ITokenService tokenService;


    @Override
    public PageInfo selCourseByTrainer(String courseName, Integer pageIndex, HttpServletRequest request) {
        if (Objects.isNull(pageIndex)){
            pageIndex = 1;
        }
        PageHelper.startPage(pageIndex, 10);
        QueryWrapper<Course> wrapper = new QueryWrapper();
        wrapper.ne("del", true);
        if (!Objects.isNull(courseName) && !courseName.equals("")){
            wrapper.like("course_name", courseName);
        }
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        if (jwtUser.getRoles().equals(88)){
            Trainer trainer = tokenService.getTrainerOfToken(request);
            log.info("trainer:{}",trainer);
            if (!Objects.isNull(trainer.getId())){
                wrapper.eq("trainer_id", trainer.getId());
            }else {
                throw new ServiceException("教练id不能为空");
            }
        }
        List<Course> courseList = courseMapper.selectList(wrapper);
        if (Objects.isNull(courseList)) {
            courseList = new ArrayList<>();
        }
        PageInfo<Course> coursePageInfo = new PageInfo<>(courseList);
        coursePageInfo.setList(courseList);
        return coursePageInfo;
    }
}
