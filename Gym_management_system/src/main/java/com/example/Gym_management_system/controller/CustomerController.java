package com.example.Gym_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.Gym_management_system.commons.Constants;
import com.example.Gym_management_system.commons.result.R;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.pojo.mapstruct.CustomerMapstruct;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerByIdVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerVo;
import com.example.Gym_management_system.service.ICustomerService;
import com.example.Gym_management_system.service.ITokenService;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import com.example.Gym_management_system.utils.JWTUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/Gym_management_system/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;
    @Autowired
    ITokenService tokenService;
    @PostMapping("addCustomer")
    public R addCustomer(@RequestBody AddCustomerVo addCustomerVo) {
        log.info("前端发来新增用户请求：{}", addCustomerVo);
        customerService.addCustomer(addCustomerVo);
        return R.ok("添加成功");
    }

    @PostMapping("getCustomerList")
    public R getCustomerList(@RequestBody SelCustomerVo selCustomerVo) {
        log.info("打印查询条件：,{}", selCustomerVo);
        PageInfo pageInfo = customerService.getCustomerList(selCustomerVo.getPageIndex(), selCustomerVo);
        return R.ok().data("pageInfo", pageInfo);
    }

    @GetMapping("selCustomerById/{id}")
    public R selCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getById(id);
        SelCustomerByIdVo selCustomerByIdVo = CustomerMapstruct.INSTANCE.dtoIdCustomerToVo(customer);
        return R.ok().data("customer", selCustomerByIdVo);
    }

    @PostMapping("updateCustomer")
    public R updateCustomer(@RequestBody Customer customer) {
        log.info(("前端发来修改员工数据{}"), customer);
        boolean b = customerService.updateById(customer);
        return R.ok();
    }

    @DeleteMapping("deleteCustomer/{id}")
    public R deleteCustomer(@PathVariable Integer id,HttpServletRequest  request) {
        // todo 逻辑删除和物理删除
        if (tokenService.getTokenOk(request)){
            return R.ok("权限不足，不能执行删除操作");
        }
        boolean b = customerService.removeById(id);
        return R.ok("删除成功");
    }

    @GetMapping("rePasswordById/{id}")
    public R rePasswordById(@PathVariable Integer id, HttpServletRequest request) {
        boolean b = customerService.rePasswordById(request, id);
        log.info("重置密码结果为：{}", b);
        if (!b) {
            return R.ok("重置密码失败");
        }
        return R.ok("重置密码成功");
    }

    @GetMapping("getCustomerByToken")
    public R getCustomerByToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        JWTLoginEntity jwtUser = JWTUtil.getJWTUser(token);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", jwtUser.getUsername());
        Customer one = customerService.getOne(wrapper);
        return R.ok().data("imageFrom", one);
    }

    @GetMapping("countCustomers")
    public R countCustomers() {
        int i = customerService.countCustomers();
        return R.ok().data("i", i);
    }

    @GetMapping("CustomersClock")
    public R CustomersClock(HttpServletRequest request) {
        boolean b = customerService.CustomersClock(request);
        if (b){
            return R.ok("打卡成功，欢迎光临");
        }
        return R.ok("打卡失败");
    }

    @GetMapping("CustomersClockOut")
    public R CustomersClockOut(HttpServletRequest request) {
        boolean b = customerService.CustomersClockOut(request);
        if (b){
            return R.ok("离开成功，欢迎下次光临");
        }
        return R.ok("操作失败");
    }
}
