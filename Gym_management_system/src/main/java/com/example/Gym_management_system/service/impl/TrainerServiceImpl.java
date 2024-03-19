package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.pojo.mapstruct.CustomerMapstruct;
import com.example.Gym_management_system.pojo.mapstruct.TrainerMapstruct;
import com.example.Gym_management_system.pojo.vo.customer.CustomerListVo;
import com.example.Gym_management_system.pojo.vo.trainer.AddTrainerVo;
import com.example.Gym_management_system.pojo.vo.trainer.SelTrainerVo;
import com.example.Gym_management_system.pojo.vo.trainer.TrainerListVo;
import com.example.Gym_management_system.service.ITrainerService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzy
 * @since 2023-09-29
 */
@Service //业务注解
@Transactional //事务注解
@Slf4j
public class TrainerServiceImpl extends ServiceImpl<TrainerMapper, Trainer> implements ITrainerService {

    @Autowired
    TrainerMapper trainerMapper;

    @Override
    public PageInfo getTrainerList(Integer pageIndex, SelTrainerVo selTrainerVo) {
        if (Objects.isNull(pageIndex)) {
            pageIndex = 1;
        }
        PageHelper.startPage(pageIndex, 10);

//        遍历查询条件中不为空的值
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(selTrainerVo)) {
            for (Field field : selTrainerVo.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!Objects.isNull(field.get(selTrainerVo)) & !field.getName().equals("pageIndex") & field.get(selTrainerVo) != "") {
                        if (field.getName().equals("create_time")){
                            wrapper.gt(field.getName(), field.get(selTrainerVo));
                        }else if (field.getName().equals("nickname")||field.getName().equals("username")||field.getName().equals("phone")){
                            wrapper.like(field.getName(), field.get(selTrainerVo));
                        }else {
                            wrapper.eq(field.getName(), field.get(selTrainerVo));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        List<Trainer> trainers = trainerMapper.selectList(wrapper);
//        防止查询结果为空
        if (Objects.isNull(trainers)) {
            trainers = new ArrayList<>();
        }
//        先将total赋值
        PageInfo trainerPageInfo = new PageInfo<>(trainers);
        ArrayList<TrainerListVo> trainerListVos = new ArrayList<>();
        trainers.forEach(trainer -> {
            TrainerListVo trainerListVo = TrainerMapstruct.INSTANCE.dtoToListVo(trainer);
            if (trainerListVo != null) {
                trainerListVos.add(trainerListVo);
            }
        });
//        再将查询结果返回
        trainerPageInfo.setList(trainerListVos);
        return trainerPageInfo;
    }

    @Override
    public void addTrainer(AddTrainerVo addTrainerVo) {
        if (Objects.isNull(addTrainerVo)) {
            throw new ServiceException("添加用户为空");
        }
//        查询一次用户名，不允许相同的用户名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", addTrainerVo.getPhone());
        Trainer trainer = trainerMapper.selectOne(wrapper);
        if (!Objects.isNull(trainer)) {
            throw new ServiceException("该用户已经注册过了");
        }
//        vo传入实体类，实体类写入入职时间、头像地址（默认）、密码（默认）、用户名（默认为手机号）
        Trainer trainer1 = TrainerMapstruct.INSTANCE.addVoToDao(addTrainerVo);
        trainer1.setUsername(addTrainerVo.getPhone());
        trainer1.setAvatarAddress(Constants.AVATAR_ADDRESS);
        trainer1.setPassword(Constants.WORKER_PASSWORD);
        trainer1.setRoles(88);
        int insert = trainerMapper.insert(trainer1);
    }

    @Override
    public boolean rePasswordById(HttpServletRequest request, Integer id) {
        //获取请求头
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTUtil.parseToken(token);
        //解析JWT令牌
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        if (Objects.isNull(jwtUser)) {
            throw new ServiceException("当前登录用户为空");
        }
        // 解析token，roles不为1/2终止进程
        if (!jwtUser.getRoles().equals(1) && !jwtUser.getRoles().equals(2)) {
            return false;
        }
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Trainer trainer = new Trainer();
        trainer.setPassword(Constants.WORKER_PASSWORD);
        trainerMapper.update(trainer, wrapper);
        return true;
    }
}
