package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.Gym_management_system.entity.Menu;
import com.example.Gym_management_system.entity.RoleMenu;
import com.example.Gym_management_system.mapper.MenuMapper;
import com.example.Gym_management_system.mapper.RoleMenuMapper;
import com.example.Gym_management_system.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-02-18
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<Menu> getMenuList(Integer roles) {
//        一级菜单
        List<Menu> menus = roleMenuMapper.selectByMenuId(roles, 0);
//        子菜单
        setMenuChildrenByRoles(roles, menus);
        return menus;
    }

    private void setMenuChildrenByRoles(Integer roles, List<Menu> menus) {
        if (!Objects.isNull(menus)){
            for (Menu menu : menus) {
                List<Menu> menus1 = roleMenuMapper.selectByMenuId(roles, menu.getMenuId());
                menu.setChildren(menus1);
//                递归
                setMenuChildrenByRoles(roles,menus1);
            }
        }
    }


}
