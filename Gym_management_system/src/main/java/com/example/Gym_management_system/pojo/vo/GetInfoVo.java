package com.example.Gym_management_system.pojo.vo;

import com.example.Gym_management_system.entity.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetInfoVo {
    @ApiModelProperty("头像地址")
    private String avatarAddress;

    @ApiModelProperty("工作类型")
    private String roles;

    @ApiModelProperty("昵称")
    private String nickname;

    private List<Menu> menuList;
}
