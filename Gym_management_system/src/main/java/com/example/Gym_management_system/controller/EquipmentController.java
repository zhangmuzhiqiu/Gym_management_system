package com.example.Gym_management_system.controller;

import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Equipment;
import com.example.Gym_management_system.pojo.vo.equipment.SelEquipmentVo;
import com.example.Gym_management_system.service.IEquipmentService;
import com.example.Gym_management_system.service.ITokenService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzy
 * @since 2024-01-10
 */
@RestController
@Slf4j
@RequestMapping("/Gym_management_system/equipment")
public class EquipmentController {
    @Autowired
    IEquipmentService equipmentService;
    @Autowired
    ITokenService tokenService;
    @PostMapping("addEquipment")
    public R addEquipment(MultipartFile avatar,
                                 String name,
                                 Boolean state,
                                 Boolean type,
                                 Integer location) {
        equipmentService.addEquipment(avatar, name,type, state, location);
        return R.ok();
    }

    @PostMapping("addEquipmentInBulk")
    public R addEquipmentInBulk(MultipartFile avatar,
                                       String name,
                                       Boolean state,
                                       Boolean type,
                                       Integer location,
                                       Integer num) {
        equipmentService.addEquipmentInBulk(avatar, name,type, state, location, num);
        return R.ok();
    }


    @PostMapping("updateEquipment")
    public R updateEquipment(MultipartFile avatar,
                             String name,
                             Boolean state,
                             Integer location,
                             Integer id) {
        equipmentService.updateEquipment(avatar,name,state,location,id);
        return R.ok();
    }

    @DeleteMapping("delEquipment/{id}")
    public R delEquipment(@PathVariable Integer id, HttpServletRequest request) {
        if (tokenService.getTokenOk(request)){
            return R.ok("权限不足，不能执行删除操作");
        }
        equipmentService.removeById(id);
        return R.ok();
    }

    @PostMapping("equipmentList")
    public R equipmentList(@RequestBody SelEquipmentVo selEquipmentVo) {
        log.info("打印查询数据:{}", selEquipmentVo);
        PageInfo equipmentPageInfo = equipmentService.equipmentList(selEquipmentVo);
        return R.ok().data("pageInfo",equipmentPageInfo);
    }

    @GetMapping("selEquipmentById/{id}")
    public R selEquipmentById(@PathVariable Integer id) {
        log.info("打印查询数据:{}", id);
        Equipment equipment = equipmentService.getById(id);
        return R.ok().data("equipment", equipment);
    }
}
