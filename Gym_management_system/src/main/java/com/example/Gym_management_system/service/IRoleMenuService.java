package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.Menu;
import com.example.Gym_management_system.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-02-18
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    List<Menu> getMenuList(Integer roles);
}
