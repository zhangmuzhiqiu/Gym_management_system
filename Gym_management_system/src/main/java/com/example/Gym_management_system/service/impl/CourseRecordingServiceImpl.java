package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.entity.Course;
import com.example.Gym_management_system.entity.CourseRecording;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.CourseMapper;
import com.example.Gym_management_system.mapper.CourseRecordingMapper;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.pojo.mapstruct.CourseMapstruct;
import com.example.Gym_management_system.pojo.mapstruct.WorkerMapstruct;
import com.example.Gym_management_system.pojo.vo.course.CourseRecordingDto;
import com.example.Gym_management_system.pojo.vo.course.SelCourseRecordingListVo;
import com.example.Gym_management_system.pojo.vo.worker.WorkerListVo;
import com.example.Gym_management_system.service.ICourseRecordingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
@Service //业务注解
@Transactional //事务注解
@Slf4j
public class CourseRecordingServiceImpl extends ServiceImpl<CourseRecordingMapper, CourseRecording> implements ICourseRecordingService {
    @Autowired
    CourseRecordingMapper courseRecordingMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TrainerMapper trainerMapper;

    @Transactional
    @Override
    public void updateCourseRecording(CourseRecording courseRecording) {
        if (Objects.isNull(courseRecording)) {
            throw new ServiceException("courseRecording为空");
        }
//        判断场地和时间是否同时冲突
        if (!Objects.isNull(courseRecording.getLocationId())) {
            courseRecording.setStopTime(courseRecording.getBeginTime().plus(2, ChronoUnit.HOURS));
            if (!Objects.isNull(courseRecording.getBeginTime())) {
                QueryWrapper wrapper = new QueryWrapper<>();
                wrapper.eq("begin_time", courseRecording.getBeginTime());
                wrapper.eq("location_id", courseRecording.getLocationId());
                CourseRecording courseRecording1 = courseRecordingMapper.selectOne(wrapper);
                if (!Objects.isNull(courseRecording1)) {
//                替换时间
                    CourseRecording courseRecording2 = courseRecordingMapper.selectById(courseRecording.getId());
                    courseRecording1.setBeginTime(courseRecording2.getBeginTime());
                    courseRecording1.setStopTime(courseRecording2.getStopTime());
                    courseRecordingMapper.updateById(courseRecording1);
                }
            }
        }
        courseRecordingMapper.updateById(courseRecording);

    }

    @Override
    public PageInfo selCourseRecordingList(SelCourseRecordingListVo selCourseRecordingListVo) {
        if (Objects.isNull(selCourseRecordingListVo.getPageIndex())){
            selCourseRecordingListVo.setPageIndex(1);
        }
        PageHelper.startPage(selCourseRecordingListVo.getPageIndex(), 10);
        QueryWrapper wrapper = new QueryWrapper();
        //        查询trainerId
        if (!Objects.isNull(selCourseRecordingListVo.getCourseId())){
            wrapper.eq("course_id",selCourseRecordingListVo.getCourseId());
        }else if (!Objects.isNull(selCourseRecordingListVo.getTrainerId()) &&Objects.isNull(selCourseRecordingListVo.getCourseId())){
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("trainer_id",selCourseRecordingListVo.getTrainerId());
            List<Course> list = courseMapper.selectList(wrapper1);
            if (!Objects.isNull(list)){
                List<Integer> list1 = null;
                list.forEach(course -> {
                    list1.add(course.getId());
                });
                wrapper.in("course_id", list1);
            }
        }
        if (!Objects.isNull(selCourseRecordingListVo.getLocationId())){
            wrapper.eq("location_id",selCourseRecordingListVo.getLocationId());
        }

        for (Field field : selCourseRecordingListVo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!Objects.isNull(field.get(selCourseRecordingListVo)) & !field.getName().equals("pageIndex") & field.get(selCourseRecordingListVo) != "") {
                    if (field.getName().equals("time")){
                        wrapper.like("begin_time", field.get(selCourseRecordingListVo));
                    }else if (!field.getName().equals("trainerId") && !field.getName().equals("courseId") && !field.getName().equals("locationId")){
                        wrapper.eq(field.getName(), field.get(selCourseRecordingListVo));
                    }
                    log.info("打印查询条件：{},{}",field.getName(), field.get(selCourseRecordingListVo));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        wrapper.eq("del",false);
        List<CourseRecording> courseRecordings = courseRecordingMapper.selectList(wrapper);
        log.info("CourseRecording查询结果：{}", courseRecordings);
//        防止查询结果为空
        if (Objects.isNull(courseRecordings)) {
            courseRecordings = new ArrayList<>();
        }

//        先将total赋值
        PageInfo courseRecordingPageInfo = new PageInfo<>(courseRecordings);
        ArrayList<CourseRecordingDto> courseRecordingDtos = new ArrayList<>();
        courseRecordings.forEach(courseRecording -> {
            CourseRecordingDto courseRecordingDto = CourseMapstruct.INSTANCE.daoToDto(courseRecording);
            Course course = courseMapper.selectById(courseRecording.getCourseId());
            if (!Objects.isNull(course)) {
                courseRecordingDto.setCourseName(course.getCourseName());
                courseRecordingDto.setText(course.getText());
                Trainer trainer = trainerMapper.selectById(course.getTrainerId());
                if (!Objects.isNull(trainer)){
                    courseRecordingDto.setNickname(trainer.getNickname());
                }
            }
            if (courseRecordingDto != null) {
                courseRecordingDtos.add(courseRecordingDto);
            }
        });

//        再将查询结果返回
        courseRecordingPageInfo.setList(courseRecordingDtos);
        log.info("打印courseRecordingDtos：{}", courseRecordingDtos);
        return courseRecordingPageInfo;
    }
}
