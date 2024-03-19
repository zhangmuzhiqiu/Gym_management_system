package com.example.Gym_management_system.pojo.mapstruct;

import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.pojo.vo.customer.CustomerListVo;
import com.example.Gym_management_system.pojo.vo.trainer.AddTrainerVo;
import com.example.Gym_management_system.pojo.vo.trainer.SelTrainerByIdVo;
import com.example.Gym_management_system.pojo.vo.trainer.TrainerListVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainerMapstruct {

    TrainerMapstruct INSTANCE = Mappers.getMapper(TrainerMapstruct.class);

    TrainerListVo dtoToListVo(Trainer trainer);

    Trainer addVoToDao(AddTrainerVo addTrainerVo);

    SelTrainerByIdVo dtoIdTrainerToVo(Trainer trainer);
}

