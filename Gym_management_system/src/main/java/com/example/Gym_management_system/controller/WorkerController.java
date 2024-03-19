package com.example.Gym_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.pojo.mapstruct.WorkerMapstruct;
import com.example.Gym_management_system.pojo.vo.GetInfoVo;
import com.example.Gym_management_system.pojo.vo.LoginVo;
import com.example.Gym_management_system.pojo.vo.worker.AddWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.SelWorkerByIdVo;
import com.example.Gym_management_system.pojo.vo.worker.SelWorkerVo;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.service.IWorkerService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zzy
 * @since 2023-09-30
 */
@RestController
@Slf4j
@RequestMapping("/Gym_management_system/worker")
public class WorkerController {
    @Autowired
    IWorkerService workerService;
    @Autowired
    ITokenService tokenService;
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        log.info("前端发来登录数据{}", loginVo);
        String token = workerService.Login(loginVo);
        return R.ok().data("token", token);
    }

    @GetMapping("getInfo")
    public R getInfo(String token) throws Exception {
        log.info(("前端发来token{}"), token);
        GetInfoVo getInfoVo = workerService.workerGetInfo(token);
        return R.ok().data("avatarAddress", getInfoVo.getAvatarAddress())
                .data("nickname", getInfoVo.getNickname())
                .data("roles", getInfoVo.getRoles())
                .data("menuList",getInfoVo.getMenuList());
    }

    @PostMapping("addWorker")
    public R addWorker(@RequestBody AddWorkerVo addWorkerVo) {
        log.info("前端发来新增员工请求{}", addWorkerVo);
        workerService.addWorker(addWorkerVo);
        return R.ok("添加成功");
    }

    @PostMapping("workerList")
    public R getWorkerList(@RequestBody SelWorkerVo selWorkerVo) {
        log.info("打印查询条件：,{}", selWorkerVo);
        PageInfo pageInfo = workerService.workerGetList(selWorkerVo.getPageIndex(), selWorkerVo);
        log.info("打印查询结果：{}", pageInfo);
        return R.ok().data("pageInfo", pageInfo);
    }

    @PostMapping("loginOut")
    public R LoginOur(HttpServletRequest request, HttpServletResponse response) {
        //获取请求头
        String token = request.getHeader(Constants.TOKEN_HEADER);
        //检验token合法性
        JWTUtil.parseToken(token);
        // 设置Cookie对象的MaxAge属性为0，表示立即过期
        Cookie cookie = new Cookie(Constants.TOKEN_HEADER, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return R.ok();
    }

    @GetMapping("selWorkerById/{id}")
    public R selWorkerById(@PathVariable Integer id) {
        Worker worker = workerService.getById(id);
        SelWorkerByIdVo selWorkerByIdVo = WorkerMapstruct.INSTANCE.dtoIdWorkerToVo(worker);
        return R.ok().data("worker", selWorkerByIdVo);
    }

    @PostMapping("updateWorker")
    public R updateWorker(@RequestBody Worker worker) {
        log.info(("前端发来修改员工数据{}"), worker);
        boolean b = workerService.updateById(worker);
        return R.ok();
    }

    @DeleteMapping("deleteWorker/{id}")
    public R deleteWorker(@PathVariable Integer id,HttpServletRequest request) {
        // todo 逻辑删除和物理删除
        if (tokenService.getTokenOk(request)){
            return R.ok("权限不足，不能执行删除操作");
        }
        boolean b = workerService.removeById(id);
        return R.ok("删除成功");
    }

    @GetMapping("rePasswordById/{id}")
    public R rePasswordById(@PathVariable Integer id, HttpServletRequest request) {
        boolean b = workerService.rePasswordById(request, id);
        log.info("重置密码结果为：{}", b);
        if (!b) {
            return R.ok("重置密码失败");
        }
        return R.ok("重置密码成功");
    }

    @PostMapping("upload")
    public R fileUpload(MultipartFile avatar,
                        String nickname,
                        String phone,
                        String password,
                        String rePassword,
                        HttpServletRequest request) {
        workerService.updateWorker(avatar, nickname, phone, password, rePassword, request);
        return R.ok("头像上传成功");
    }

    @GetMapping("getWorkerByToken")
    public R getWorkerByToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        Worker one = workerService.getOne(wrapper);
        return R.ok().data("imageFrom", one);
    }
}
