package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Equipment;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.EquipmentMapper;
import com.example.Gym_management_system.pojo.vo.equipment.SelEquipmentVo;
import com.example.Gym_management_system.service.IEquipmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzy
 * @since 2024-01-10
 */
@Service //业务注解
@Transactional //事务注解
@Slf4j
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements IEquipmentService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    @Transactional
    public String fileUpload(MultipartFile avatar) {
        if (Objects.isNull((avatar))) {
            throw new ServiceException("文件上传异常：上传文件不能为空");
        }
        //获取文件名称
        String filename = avatar.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            throw new ServiceException("文件上传异常:文件名称不能为空");
        }
        //判断文件的类型
        if (!Constants.FILE_TYPES.contains(avatar.getContentType())) {
            throw new ServiceException("文件上传异常:文件类型只能是：" + Constants.FILE_TYPES);
        }
        //判断大小
        long size = avatar.getSize();//字节
        if (size > Constants.FILE_SIZE) {
            throw new ServiceException("文件上传异常:文件太大，不能超过：" + Constants.FILE_SIZE / 1024 + "MB");
        }
        //设置文件上传的地址
        //获取工作目录
        String workDir = System.getProperty("user.dir");
        String savePath = workDir + File.separator + "Gym_management_system" + File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources" + File.separator + "images";
        log.info("文件存储的路径：{}", savePath);
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();//创建多次目录
        }
        //设置头像的新名称
        //截取文件的后缀
        String type = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID().toString() + type;
        log.info("上传的文件名称:{}", filename);
        //保存文件
        try {
            avatar.transferTo(new File(saveFile, filename));
            //将文件名返回
            String image_address = ("images/" + filename);
            return image_address;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传异常:" + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public void addEquipment(MultipartFile avatar, String name,Boolean type, Boolean state, Integer location) {
        Equipment equipment = new Equipment();
        if (!Objects.isNull(avatar)) {
            String image_address = fileUpload(avatar);
            equipment.setImageAddress(image_address);
        }
        if (!Objects.isNull(name) && !name.equals("")) {
            equipment.setName(name);
        }
        if (!Objects.isNull(state)) {
            equipment.setState(state);
        }
        if (!Objects.isNull(location)) {
            equipment.setLocation(location);
        }
        if (!Objects.isNull(type)){
            equipment.setType(type);
        }
        equipment.setUid(String.valueOf(UUID.randomUUID()));
        equipment.setCreateTime(LocalDate.now());
        equipmentMapper.insert(equipment);
    }

    @Transactional
    @Override
    public void addEquipmentInBulk(MultipartFile avatar, String name,Boolean type, Boolean state, Integer location, Integer num) {
        String image_address = null;
        if (!Objects.isNull(avatar)) {
            image_address = fileUpload(avatar);
        }
        if (num >= 0) {
            while (num > 0) {
                Equipment equipment = new Equipment();
                log.info("equipment:{}",equipment);
                if (!Objects.isNull(image_address)) {
                    equipment.setImageAddress(image_address);
                }
                if (!Objects.isNull(name) && !name.equals("")) {
                    equipment.setName(name);
                }
                if (!Objects.isNull(state)) {
                    equipment.setState(state);
                }
                if (!Objects.isNull(location)) {
                    equipment.setLocation(location);
                }
                if (!Objects.isNull(type)) {
                    equipment.setType(type);
                }
                equipment.setCreateTime(LocalDate.now());
                equipment.setUid(String.valueOf(UUID.randomUUID()));
                equipmentMapper.insert(equipment);
                num--;
            }
        } else {
            throw new ServiceException("批量添加数量错误");
        }


    }


    @Override
    public PageInfo<Equipment> equipmentList(SelEquipmentVo selEquipmentVo) {
        if (Objects.isNull(selEquipmentVo.getPageIndex())) {
            selEquipmentVo.setPageIndex(1);
        }
        PageHelper.startPage(selEquipmentVo.getPageIndex(), 10);
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(selEquipmentVo.getUid()) && !selEquipmentVo.getUid().equals("")){
            wrapper.like("uid", selEquipmentVo.getUid());
        }
        if (!Objects.isNull(selEquipmentVo.getLocation())){
            wrapper.eq("location", selEquipmentVo.getLocation());
        }
        if (!Objects.isNull(selEquipmentVo.getState())){
            wrapper.eq("state", selEquipmentVo.getState());
        }
        if (!Objects.isNull(selEquipmentVo.getName()) && !selEquipmentVo.getName().equals("")){
            wrapper.like("name", selEquipmentVo.getName());
        }
        if (!Objects.isNull(selEquipmentVo.getCreate_time())){
            wrapper.gt("create_time", selEquipmentVo.getCreate_time());
        }
        if (!Objects.isNull(selEquipmentVo.getType())){
            wrapper.eq("type", selEquipmentVo.getType());
        }
        List<Equipment> equipmentList = equipmentMapper.selectList(wrapper);
//      防空
        if (Objects.isNull(equipmentList)){
            equipmentList = new ArrayList<>();
        }
        PageInfo<Equipment> equipmentPageInfo = new PageInfo<>(equipmentList);
        equipmentPageInfo.setList(equipmentList);
        return equipmentPageInfo;
    }

    @Override
    public void updateEquipment(MultipartFile avatar, String name, Boolean state, Integer location,Integer id) {
        Equipment equipment = new Equipment();
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(id)){
//            wrapper.eq("id",id);
            equipment.setId(id);
        }else {
            throw new ServiceException("修改的内容不存在");
        }
        if (!Objects.isNull(avatar)) {
            String image_address = fileUpload(avatar);
            equipment.setImageAddress(image_address);
        }
        if (!Objects.isNull(name) && !name.equals("")) {
            equipment.setName(name);
        }
        if (!Objects.isNull(state)) {
            equipment.setState(state);
        }
        if (!Objects.isNull(location)) {
            equipment.setLocation(location);
        }
        equipmentMapper.updateById(equipment);
    }
}
