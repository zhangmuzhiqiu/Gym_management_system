package com.example.Gym_management_system.mapper;

import com.example.Gym_management_system.entity.Menu;
import com.example.Gym_management_system.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzy
 * @since 2024-02-18
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Menu> selectByMenuId(Integer roles,Integer pid);
}
