package com.example.Gym_management_system.pojo.mapstruct;

import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.pojo.vo.customer.AddCustomerVo;
import com.example.Gym_management_system.pojo.vo.customer.CustomerListVo;
import com.example.Gym_management_system.pojo.vo.customer.SelCustomerByIdVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapstruct {

    CustomerMapstruct INSTANCE = Mappers.getMapper(CustomerMapstruct.class);

    Customer addVoToDao(AddCustomerVo addCustomerVo);

    CustomerListVo dtoToListVo(Customer customer);

    SelCustomerByIdVo dtoIdCustomerToVo(Customer customer);

}
