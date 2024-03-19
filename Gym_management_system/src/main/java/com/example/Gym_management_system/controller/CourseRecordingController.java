package com.example.Gym_management_system.controller;

import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.CourseRecording;
import com.example.Gym_management_system.pojo.vo.course.SelCourseRecordingListVo;
import com.example.Gym_management_system.service.ICourseRecordingService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
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
@RequestMapping("/Gym_management_system/courseRecording")
public class CourseRecordingController {
    @Autowired
    ICourseRecordingService courseRecordingService;


    @PostMapping("addCourseRecording")
    public R addCourseRecording(@RequestBody CourseRecording courseRecording) {
        log.info("打印addCourseRecording:{}", courseRecording);
        courseRecording.setStopTime(courseRecording.getBeginTime().plus(2, ChronoUnit.HOURS));
        courseRecording.setDel(false);
        courseRecording.setPass(false);
        courseRecording.setDone(false);
        courseRecordingService.save(courseRecording);
        return R.ok();
    }

    @PostMapping("updateCourseRecording")
    public R updateCourseRecording(@RequestBody CourseRecording courseRecording) {
        log.info("打印updateCourseRecording:{}", courseRecording);
        courseRecordingService.updateCourseRecording(courseRecording);
        return R.ok();
    }


    @GetMapping("selCourseRecordingById/{id}")
    public R selCourseRecordingById(@PathVariable Integer id) {
        log.info("打印selCourseRecordingById:{}", id);
        Optional<CourseRecording> optById = courseRecordingService.getOptById(id);
        return R.ok().data("CourseRecording", optById.get());
    }


    @PostMapping("selCourseRecordingList")
    public R selCourseRecordingList(@RequestBody SelCourseRecordingListVo selCourseRecordingListVo) {
        log.info("打印selCourseRecordingList:{}", selCourseRecordingListVo);
        PageInfo pageInfo = courseRecordingService.selCourseRecordingList(selCourseRecordingListVo);
        return R.ok().data("pageInfo",pageInfo);
    }


}
