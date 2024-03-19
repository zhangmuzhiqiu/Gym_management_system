package com.example.Gym_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.pojo.vo.GetInfoVo;
import com.example.Gym_management_system.pojo.vo.LoginVo;
import com.example.Gym_management_system.pojo.vo.worker.AddWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.SelWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.WorkerListVo;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zzy
 * @since 2023-09-30
 */
public interface IWorkerService extends IService<Worker> {

    GetInfoVo workerGetInfo(String token) throws Exception;

    void addWorker(AddWorkerVo addWorkerVo);

    PageInfo<WorkerListVo> workerGetList(Integer pageIndex, SelWorkerVo selWorkerVo);

    boolean rePasswordById(HttpServletRequest request, Integer id);

    @Transactional
    void fileUpload(MultipartFile avatar, HttpServletRequest request);

    @Transactional
    void updateWorker(MultipartFile avatar, String nickname, String phone, String password, String rePassword, HttpServletRequest request);

    void rePasswordByToken(String password, String rePassword, HttpServletRequest request);

    String Login(LoginVo loginVo);
}
