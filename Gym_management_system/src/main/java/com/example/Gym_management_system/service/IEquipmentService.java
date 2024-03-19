package com.example.Gym_management_system.service;

import com.example.Gym_management_system.entity.Equipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.pojo.vo.equipment.SelEquipmentVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzy
 * @since 2024-01-10
 */
public interface IEquipmentService extends IService<Equipment> {

    @Transactional
    String fileUpload(MultipartFile avatar);

    void addEquipment(MultipartFile avatar, String name,Boolean type, Boolean state, Integer location);

    void addEquipmentInBulk(MultipartFile avatar, String name,Boolean type, Boolean state, Integer location, Integer num);

    PageInfo<Equipment> equipmentList(SelEquipmentVo selEquipmentVo);

    void updateEquipment(MultipartFile avatar, String name, Boolean state, Integer location,Integer id);
}

