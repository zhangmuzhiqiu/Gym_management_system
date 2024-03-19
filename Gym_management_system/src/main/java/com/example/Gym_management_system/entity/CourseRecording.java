package com.example.Gym_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("c_course_recording")
@ApiModel(value = "CourseRecording对象", description = "")
public class CourseRecording implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("场地id")
    private Integer locationId;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime stopTime;

    @ApiModelProperty("是否完成")
    private Boolean done;

    @ApiModelProperty("是否通过")
    private Boolean pass;

    @ApiModelProperty("是否删除")
    private Boolean del;
}
