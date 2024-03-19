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
public class SelCourseRecordingListVo {

    private Integer pageIndex;

    @ApiModelProperty("场地id")
    private Integer locationId;

    @ApiModelProperty("是否通过")
    private Boolean pass;

    @ApiModelProperty("是否完成")
    private Boolean done;

    @ApiModelProperty("时间")
    private LocalDate time;

    @ApiModelProperty("教练id")
    private Integer trainerId;

    @ApiModelProperty("课程id")
    private Integer courseId;
}
