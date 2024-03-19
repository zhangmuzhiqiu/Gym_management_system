package com.example.Gym_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.pojo.vo.trainer.AddTrainerVo;
import com.example.Gym_management_system.pojo.vo.trainer.SelTrainerVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zzy
 * @since 2023-09-29
 */
public interface ITrainerService extends IService<Trainer> {

    PageInfo getTrainerList(Integer pageIndex, SelTrainerVo selTrainerVo);

    void addTrainer(AddTrainerVo addTrainerVo);

    boolean rePasswordById(HttpServletRequest request, Integer id);
}
