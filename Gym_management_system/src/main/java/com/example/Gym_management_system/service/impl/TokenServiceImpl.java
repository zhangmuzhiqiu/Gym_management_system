package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service //业务注解
@Transactional //事务注解
@Slf4j
public class TokenServiceImpl implements ITokenService {

    @Autowired
    TrainerMapper trainerMapper;

    //    通过token获得trainer信息
    public Trainer getTrainerOfToken(HttpServletRequest request) {
        //获取请求头
        String token = request.getHeader(Constants.TOKEN_HEADER);
        //检验token合法性
        JWTUtil.parseToken(token);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("username", jwtUser.getUsername());
        wrapper1.eq("password", jwtUser.getPassword());
        Trainer trainer = trainerMapper.selectOne(wrapper1);
        return trainer;
    }

    //    通过token获得身份判断操作合法性
    public boolean getTokenOk(HttpServletRequest request) {
        //获取请求头
        String token = request.getHeader(Constants.TOKEN_HEADER);
        //检验token合法性
        JWTUtil.parseToken(token);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        if (jwtUser.getRoles().equals(1) || jwtUser.getRoles().equals(2)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean getTokenOkByTrainer(HttpServletRequest request) {
        //获取请求头
        String token = request.getHeader(Constants.TOKEN_HEADER);
        //检验token合法性
        JWTUtil.parseToken(token);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        if (jwtUser.getRoles().equals(1) || jwtUser.getRoles().equals(2)||jwtUser.getRoles().equals(88)) {
            return false;
        }
        return true;
    }
}
