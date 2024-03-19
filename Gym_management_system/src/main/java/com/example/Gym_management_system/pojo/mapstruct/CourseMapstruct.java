package com.example.Gym_management_system.pojo.mapstruct;

import com.example.Gym_management_system.entity.CourseRecording;
import com.example.Gym_management_system.pojo.vo.course.CourseRecordingDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapstruct {

    CourseMapstruct INSTANCE = Mappers.getMapper(CourseMapstruct.class);

    CourseRecordingDto daoToDto(CourseRecording courseRecording);
}
