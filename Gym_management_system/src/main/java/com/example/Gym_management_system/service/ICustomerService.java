package com.example.Gym_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerVo;
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
public interface ICustomerService extends IService<Customer> {

    void addCustomer(AddCustomerVo addCustomerVo);

    PageInfo getCustomerList(Integer pageIndex, SelCustomerVo selCustomerVo);

    boolean rePasswordById(HttpServletRequest request, Integer id);

    int countCustomers();

    boolean CustomersClock(HttpServletRequest request);

    boolean CustomersClockOut(HttpServletRequest request);
}
