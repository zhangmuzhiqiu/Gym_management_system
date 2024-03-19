package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.CourseRecording;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.pojo.vo.course.SelCourseRecordingListVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
public interface ICourseRecordingService extends IService<CourseRecording> {

    @Transactional
    void updateCourseRecording(CourseRecording courseRecording);

    PageInfo selCourseRecordingList(SelCourseRecordingListVo selCourseRecordingListVo);
}
