package com.example.Gym_management_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zzy
 * @since 2024-01-26
 */
@TableName("c_course_customer")
@ApiModel(value = "CourseCustomer对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer recordingId;

    private Integer customerId;

    private Boolean del;
}
