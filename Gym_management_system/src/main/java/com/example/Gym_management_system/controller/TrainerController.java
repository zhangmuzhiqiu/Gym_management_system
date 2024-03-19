package com.example.Gym_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.pojo.mapstruct.TrainerMapstruct;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerVo;
import com.example.Gym_management_system.pojo.vo.trainer.AddTrainerVo;
import com.example.Gym_management_system.pojo.vo.trainer.SelTrainerByIdVo;
import com.example.Gym_management_system.pojo.vo.trainer.SelTrainerVo;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.service.ITrainerService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzy
 * @since 2023-09-29
 */
@RestController
@Slf4j
@RequestMapping("/Gym_management_system/trainer")
public class TrainerController {
    @Autowired
    ITrainerService trainerService;
    @Autowired
    ITokenService tokenService;

    @PostMapping("getTrainerList")
    public R getTrainerList(@RequestBody SelTrainerVo selTrainerVo) {
        log.info("打印查询条件：,{}", selTrainerVo);
        PageInfo pageInfo = trainerService.getTrainerList(selTrainerVo.getPageIndex(), selTrainerVo);
        log.info("打印查询结果：{}",pageInfo);
        return R.ok().data("pageInfo", pageInfo);
    }

    @PostMapping("addTrainer")
    public R addTrainer(@RequestBody AddTrainerVo addTrainerVo) {
        log.info("前端发来新增用户请求：{}", addTrainerVo);
        trainerService.addTrainer(addTrainerVo);
        return R.ok("添加成功");
    }

    @GetMapping("selTrainerById/{id}")
    public R selTrainerById(@PathVariable Integer id) {
        Trainer trainer = trainerService.getById(id);
        SelTrainerByIdVo selTrainerByIdVo = TrainerMapstruct.INSTANCE.dtoIdTrainerToVo(trainer);
        return R.ok().data("Trainer", selTrainerByIdVo);
    }

    @PostMapping("updateTrainer")
    public R updateTrainer(@RequestBody Trainer trainer) {
        log.info(("前端发来修改员工数据{}"), trainer);
        boolean b = trainerService.updateById(trainer);
        return R.ok();
    }

    @DeleteMapping("deleteTrainer/{id}")
    public R deleteTrainer(@PathVariable Integer id,HttpServletRequest request) {
        // todo 逻辑删除和物理删除
        if (tokenService.getTokenOk(request)){
            return R.ok("权限不足，不能执行删除操作");
        }
        boolean b = trainerService.removeById(id);
        return R.ok("删除成功");
    }

    @GetMapping("rePasswordById/{id}")
    public R rePasswordById(@PathVariable Integer id, HttpServletRequest request) {
        boolean b = trainerService.rePasswordById(request, id);
        log.info("重置密码结果为：{}", b);
        if (!b) {
            return R.ok("重置密码失败");
        }
        return R.ok("重置密码成功");
    }

    @GetMapping("getAllTrainer")
    public R getAllTrainer(){
        List<Trainer> list = trainerService.list();
        ArrayList<Trainer> trainers = new ArrayList<>();
        for (Trainer trainer : list) {
            Trainer trainer1 = new Trainer();
            trainer1.setNickname(trainer.getNickname());
            trainer1.setId(trainer.getId());
            trainers.add(trainer1);
        }
        return R.ok().data("pageInfo", trainers);
    }

    @GetMapping("getTrainerByToken")
    public R getTrainerByToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        Trainer one = trainerService.getOne(wrapper);
        return R.ok().data("imageFrom", one);
    }
}
