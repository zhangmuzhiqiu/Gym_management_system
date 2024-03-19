package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Menu;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.CustomerMapper;
import com.example.Gym_management_system.mapper.TrainerMapper;
import com.example.Gym_management_system.mapper.WorkerMapper;
import com.example.Gym_management_system.pojo.mapstruct.WorkerMapstruct;
import com.example.Gym_management_system.pojo.vo.GetInfoVo;
import com.example.Gym_management_system.pojo.vo.LoginVo;
import com.example.Gym_management_system.pojo.vo.worker.AddWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.SelWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.WorkerListVo;
import com.example.Gym_management_system.service.IRoleMenuService;
import com.example.Gym_management_system.service.IWorkerService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
 * @since 2023-09-30
 */
@Service //业务注解
@Transactional //事务注解
@Slf4j
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements IWorkerService {

    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    TrainerMapper trainerMapper;
    @Autowired
    IRoleMenuService roleMenuService;

    @Override
    public GetInfoVo workerGetInfo(String token) {
//        判断传入内容是否为空
        if (Objects.isNull(token)) {
            throw new ServiceException("当前登录失效，请重新登录");
        }
//        解析JWT令牌
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        log.info("当前token数据解析结果为：{}", jwtUser);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        wrapper.eq("password", jwtUser.getPassword());
        List<Menu> menuList = roleMenuService.getMenuList(jwtUser.getRoles());
        if (jwtUser.getRoles().equals(88)){
            Trainer trainer = trainerMapper.selectOne(wrapper);
            if (Objects.isNull(trainer)) {
                throw new ServiceException("错误的登录信息，没有当前用户");
            }
            GetInfoVo getInfoVo = WorkerMapstruct.INSTANCE.dtogetInfoToTrainer(trainer);
            getInfoVo.setMenuList(menuList);
            return getInfoVo;
        }else if (jwtUser.getRoles().equals(66)){
            Customer customer = customerMapper.selectOne(wrapper);
            if (Objects.isNull(customer)) {
                throw new ServiceException("错误的登录信息，没有当前用户");
            }
            GetInfoVo getInfoVo = WorkerMapstruct.INSTANCE.dtogetInfoToCustomer(customer);
            getInfoVo.setMenuList(menuList);
            return getInfoVo;
        }else if (!Objects.isNull(jwtUser.getRoles())){
            Worker worker = workerMapper.selectOne(wrapper);
//        判断查询结果是否为空
            if (Objects.isNull(worker)) {
                throw new ServiceException("错误的登录信息，没有当前用户");
            }
            GetInfoVo getInfoVo = WorkerMapstruct.INSTANCE.dtogetInfoToVo(worker);
            getInfoVo.setMenuList(menuList);
            return getInfoVo;
        }

        return null;
    }

    @Override
    public void addWorker(AddWorkerVo addWorkerVo) {
        if (Objects.isNull(addWorkerVo)) {
            throw new ServiceException("添加用户为空");
        }
//        查询一次用户名，不允许相同的用户名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", addWorkerVo.getPhone());
        Worker worker1 = workerMapper.selectOne(wrapper);
        if (!Objects.isNull(worker1)) {
            throw new ServiceException("该用户已经注册过了");
        }
//        vo传入实体类，实体类写入入职时间、头像地址（默认）、密码（默认）、用户名（默认为手机号）
        Worker worker = WorkerMapstruct.INSTANCE.voWorkerToEn(addWorkerVo);
        worker.setCreateTime(LocalDate.now());
        worker.setUsername(addWorkerVo.getPhone());
        worker.setAvatarAddress(Constants.AVATAR_ADDRESS);
        worker.setPassword(Constants.WORKER_PASSWORD);
        int insert = workerMapper.insert(worker);
    }

    @Override
    public PageInfo<WorkerListVo> workerGetList(Integer pageIndex, SelWorkerVo selWorkerVo) {
        if (Objects.isNull(pageIndex)) {
            pageIndex = 1;
        }
        PageHelper.startPage(pageIndex, 10);

//        遍历查询条件中不为空的值
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(selWorkerVo)) {
            for (Field field : selWorkerVo.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!Objects.isNull(field.get(selWorkerVo)) & !field.getName().equals("pageIndex") & field.get(selWorkerVo) != "") {
                        if (field.getName().equals("create_time")) {
                            wrapper.gt(field.getName(), field.get(selWorkerVo));
                        }else if (field.getName().equals("nickname")||field.getName().equals("username")||field.getName().equals("phone")){
                            wrapper.like(field.getName(), field.get(selWorkerVo));
                        }else {
                            wrapper.eq(field.getName(), field.get(selWorkerVo));
                        }
                        log.info("打印查询条件：{},{}",field.getName(), field.get(selWorkerVo));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        List<Worker> workers = workerMapper.selectList(wrapper);
        log.info("Worker查询结果：{}",workers);
//        防止查询结果为空
        if (Objects.isNull(workers)) {
            workers = new ArrayList<>();
        }
//        先将total赋值
        PageInfo workerPageInfo = new PageInfo<>(workers);
        ArrayList<WorkerListVo> workerListVos = new ArrayList<>();
        workers.forEach(worker -> {
            WorkerListVo workerListVo = WorkerMapstruct.INSTANCE.dtoWorkerToListVo(worker);
            if (workerListVo != null) {
                workerListVos.add(workerListVo);
            }
        });
//        再将查询结果返回
        workerPageInfo.setList(workerListVos);
        log.info("打印workerListVos：{}",workerListVos);
        return workerPageInfo;
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
        Worker worker = new Worker();
        worker.setPassword(Constants.WORKER_PASSWORD);
        workerMapper.update(worker, wrapper);
        return true;
    }


    @Override
    @Transactional
    public void fileUpload(MultipartFile avatar, HttpServletRequest request) {
        if (Objects.isNull((avatar))) {
            throw new ServiceException("文件上传异常：上传文件不能为空");
        }
        //获取上传文件用户数据
        String token = request.getHeader(Constants.TOKEN_HEADER);
        log.info("打印当前token{}", token);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
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
            //调用业务层修改数据
            //创建修改头像的用户对象
            QueryWrapper wrapper = new QueryWrapper<>();
            wrapper.eq("username", jwtUser.getUsername());
            wrapper.eq("password", jwtUser.getPassword());
            int row = 0;
            if (jwtUser.getRoles().equals(66)) {
                Customer worker = customerMapper.selectOne(wrapper);
                worker.setAvatarAddress("images/" + filename);
                row = customerMapper.updateById(worker);
            } else if (jwtUser.getRoles().equals(88)) {
                Trainer worker = trainerMapper.selectOne(wrapper);
                worker.setAvatarAddress("images/" + filename);
                row = trainerMapper.updateById(worker);
            } else{
                Worker worker = workerMapper.selectOne(wrapper);
                worker.setAvatarAddress("images/" + filename);
                row = workerMapper.updateById(worker);
            }

            if (row != 1) {
                throw new ServiceException("文件上传异常：上传文件失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传异常:" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateWorker(MultipartFile avatar, String nickname, String phone, String password, String rePassword, HttpServletRequest request) {
        if (!Objects.isNull(avatar)) {
            fileUpload(avatar, request);
        }
        if ((!Objects.isNull(password) && !password.equals("")) && (!Objects.isNull(rePassword) && !rePassword.equals(""))) {
            rePasswordByToken(password, rePassword, request);
        }
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        wrapper.eq("password", jwtUser.getPassword());
        if (jwtUser.getRoles().equals(66)){
            if (!Objects.isNull(nickname) && !nickname.equals("")) {
                Customer worker = new Customer();
                worker.setNickname(nickname);
                customerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("昵称不能为空");
            }
            if (!Objects.isNull(phone) && !phone.equals("")) {
                Customer worker = new Customer();
                worker.setPhone(phone);
                customerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("手机号不能为空");
            }
        }else if (jwtUser.getRoles().equals(88)){
            if (!Objects.isNull(nickname) && !nickname.equals("")) {
                Trainer worker = new Trainer();
                worker.setNickname(nickname);
                trainerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("昵称不能为空");
            }
            if (!Objects.isNull(phone) && !phone.equals("")) {
                Trainer worker = new Trainer();
                worker.setPhone(phone);
                trainerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("手机号不能为空");
            }
        }else {
            if (!Objects.isNull(nickname) && !nickname.equals("")) {
                Worker worker = new Worker();
                worker.setNickname(nickname);
                workerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("昵称不能为空");
            }
            if (!Objects.isNull(phone) && !phone.equals("")) {
                Worker worker = new Worker();
                worker.setPhone(phone);
                workerMapper.update(worker, wrapper);
            } else {
                throw new ServiceException("手机号不能为空");
            }
        }
    }

    @Override
    public void rePasswordByToken(String password, String rePassword, HttpServletRequest request) {
        if ((Objects.isNull(password) || password.equals("")) && (Objects.isNull(rePassword) || rePassword.equals(""))) {
            throw new ServiceException("重置密码异常:两次密码不能为空");
        }
        if (!password.equals(rePassword)) {
            throw new ServiceException("重置密码异常:两次密码不相同");
        }
//      解析一次token，并判断token是否合法
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        wrapper.eq("password", jwtUser.getPassword());
        if (jwtUser.getRoles().equals(66)){
            Customer worker = new Customer();
            worker.setPassword(rePassword);
            customerMapper.update(worker, wrapper);
        }else if (jwtUser.getRoles().equals(88)){
            Trainer worker = new Trainer();
            worker.setPassword(rePassword);
            trainerMapper.update(worker, wrapper);
        }else {
            Worker worker = new Worker();
            worker.setPassword(rePassword);
            workerMapper.update(worker, wrapper);
        }

    }

    @Override
    public String Login(LoginVo loginVo) {
        //        判断传入内容是否为空
        if (Objects.isNull(loginVo)) {
            throw new ServiceException("登录名和密码不能为空");
        }
//        判断查询结果是否为空
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", loginVo.getUsername());
        if (loginVo.getRadio().equals(1)){
            Customer customer = customerMapper.selectOne(wrapper);
            if (Objects.isNull(customer)) {
                throw new ServiceException("当前用户不存在");
            }
            wrapper.eq("password", loginVo.getPassword());
            customer = customerMapper.selectOne(wrapper);
            if (Objects.isNull(customer)) {
                throw new ServiceException("密码错误");
            }
            //        生成token令牌
            //        mapstruct传值
            JWTLoginEntity jwtLoginEntity = WorkerMapstruct.INSTANCE.dtoCustomerToJWT(customer);
            String token = JWTUtil.getToken(jwtLoginEntity);
            return token;
        }
        if (loginVo.getRadio().equals(2)){
            Worker worker1 = workerMapper.selectOne(wrapper);
            if (Objects.isNull(worker1)) {
                throw new ServiceException("当前用户不存在");
            }
            wrapper.eq("password", loginVo.getPassword());
            worker1 = workerMapper.selectOne(wrapper);
            if (Objects.isNull(worker1)) {
                throw new ServiceException("密码错误");
            }
            if (worker1.getRoles() > 3) {
                throw new ServiceException("你不能登录后台管理系统");
            }
            //        生成token令牌
            //        mapstruct传值
            JWTLoginEntity jwtLoginEntity = WorkerMapstruct.INSTANCE.dtoWorkerToJWT(worker1);
            String token = JWTUtil.getToken(jwtLoginEntity);
            return token;
        }
        if (loginVo.getRadio().equals(3)){
            Trainer trainer = trainerMapper.selectOne(wrapper);
            if (Objects.isNull(trainer)) {
                throw new ServiceException("当前用户不存在");
            }
            wrapper.eq("password", loginVo.getPassword());
            trainer = trainerMapper.selectOne(wrapper);
            if (Objects.isNull(trainer)) {
                throw new ServiceException("密码错误");
            }
            //        生成token令牌
            //        mapstruct传值
            JWTLoginEntity jwtLoginEntity = WorkerMapstruct.INSTANCE.dtoTrainerToJWT(trainer);
            String token = JWTUtil.getToken(jwtLoginEntity);
            return token;
        }
        return null;
    }


}
