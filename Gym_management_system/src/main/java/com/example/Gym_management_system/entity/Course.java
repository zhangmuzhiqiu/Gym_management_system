package com.example.Gym_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("c_course")
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程价格")
    private BigDecimal coursePrice;

    @ApiModelProperty("教练id")
    private Integer trainerId;

    @ApiModelProperty("介绍")
    private String text;

    @ApiModelProperty("删除")
    private Boolean del;
}
