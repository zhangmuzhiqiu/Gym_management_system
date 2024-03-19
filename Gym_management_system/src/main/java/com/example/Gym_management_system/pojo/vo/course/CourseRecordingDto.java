package com.example.Gym_management_system.pojo.vo.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRecordingDto {

    private Integer id;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime stopTime;

    @ApiModelProperty("是否完成")
    private String done;

    @ApiModelProperty("是否通过")
    private Boolean pass;

    @ApiModelProperty("教练昵称")
    private String nickname;

    @ApiModelProperty("介绍")
    private String text;
}
