package com.example.Gym_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.exception.ServiceException;
import com.example.Gym_management_system.mapper.CustomerMapper;
import com.example.Gym_management_system.pojo.mapstruct.CustomerMapstruct;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.CustomerListVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerVo;
import com.example.Gym_management_system.service.ICustomerService;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public void addCustomer(AddCustomerVo addCustomerVo) {
        if (Objects.isNull(addCustomerVo)) {
            throw new ServiceException("添加用户为空");
        }
//        查询一次用户名，不允许相同的用户名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", addCustomerVo.getPhone());
        Customer customer = customerMapper.selectOne(wrapper);
        if (!Objects.isNull(customer)) {
            throw new ServiceException("该用户已经注册过了");
        }
//        vo传入实体类，实体类写入入职时间、头像地址（默认）、密码（默认）、用户名（默认为手机号）
        Customer customer1 = CustomerMapstruct.INSTANCE.addVoToDao(addCustomerVo);
        customer1.setUsername(addCustomerVo.getPhone());
        customer1.setAvatarAddress(Constants.AVATAR_ADDRESS);
        customer1.setPassword(Constants.WORKER_PASSWORD);
        customer1.setInGym(false);
        customer1.setRoles(66);
        int insert = customerMapper.insert(customer1);
    }

    @Override
    public PageInfo getCustomerList(Integer pageIndex, SelCustomerVo selCustomerVo) {
        if (Objects.isNull(pageIndex)) {
            pageIndex = 1;
        }
        PageHelper.startPage(pageIndex, 10);

//        遍历查询条件中不为空的值
        QueryWrapper wrapper = new QueryWrapper();
        if (!Objects.isNull(selCustomerVo)) {
            for (Field field : selCustomerVo.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (!Objects.isNull(field.get(selCustomerVo)) & !field.getName().equals("pageIndex") & field.get(selCustomerVo) != "") {
                        if (field.getName().equals("create_time")){
                            wrapper.gt(field.getName(), field.get(selCustomerVo));
                        }else if (field.getName().equals("nickname")||field.getName().equals("username")||field.getName().equals("phone")){
                            wrapper.like(field.getName(), field.get(selCustomerVo));
                        }else if (field.getName().equals("times")){
                            if (field.get(selCustomerVo).equals(0)){
                                wrapper.lt(field.getName(),"1");
                            }
                            if (field.get(selCustomerVo).equals(1)){
                                wrapper.gt(field.getName(),"0");
                            }
                        }else {
                            wrapper.eq(field.getName(), field.get(selCustomerVo));
                        }

                        log.info("打印查询条件：{},{}",field.getName(), field.get(selCustomerVo));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        List<Customer> customers = customerMapper.selectList(wrapper);
//        防止查询结果为空
        if (Objects.isNull(customers)) {
            customers = new ArrayList<>();
        }
//        先将total赋值
        PageInfo customerPageInfo = new PageInfo<>(customers);
        ArrayList<CustomerListVo> customerListVos = new ArrayList<>();
        customers.forEach(customer -> {
            CustomerListVo customerListVo = CustomerMapstruct.INSTANCE.dtoToListVo(customer);
            if (customerListVo != null) {
                customerListVos.add(customerListVo);
            }
        });
//        再将查询结果返回
        customerPageInfo.setList(customerListVos);
        return customerPageInfo;
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
        Customer customer = new Customer();
        customer.setPassword(Constants.WORKER_PASSWORD);
        customerMapper.update(customer, wrapper);
        return true;
    }

    public int countCustomers() {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getInGym, true);
        Long selectCount = baseMapper.selectCount(queryWrapper);
        return selectCount != null ? selectCount.intValue() : 0;
    }

    @Override
    public boolean CustomersClock(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("username",jwtUser.getUsername());
        wrapper.eq("password",jwtUser.getPassword());
        Customer customer = customerMapper.selectOne(wrapper);
        if (customer.getInGym().equals(false)){
            customer.setInGym(true);
            customerMapper.updateById(customer);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean CustomersClockOut(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("username",jwtUser.getUsername());
        wrapper.eq("password",jwtUser.getPassword());
        Customer customer = customerMapper.selectOne(wrapper);
        if (customer.getInGym().equals(true)){
            customer.setInGym(false);
            customerMapper.updateById(customer);
            return true;
        }else {
            return false;
        }
    }
}
