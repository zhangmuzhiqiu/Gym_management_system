package com.example.Gym_management_system.entity;

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
 * @since 2024-01-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Equipment对象", description = "")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    private String imageAddress;

    @ApiModelProperty("状态")
    private Boolean state;

    @ApiModelProperty("位置")
    private Integer location;

    @ApiModelProperty("类型")
    private Boolean type;

    private String uid;

    private LocalDate createTime;

    private LocalDate leaveTime;

    private Boolean del;

}
