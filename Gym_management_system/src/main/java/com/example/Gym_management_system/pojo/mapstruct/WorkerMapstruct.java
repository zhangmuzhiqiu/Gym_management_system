package com.example.Gym_management_system.pojo.mapstruct;

import com.example.Gym_management_system.entity.Customer;
import com.example.Gym_management_system.entity.Trainer;
import com.example.Gym_management_system.entity.Worker;
import com.example.Gym_management_system.pojo.vo.GetInfoVo;
import com.example.Gym_management_system.pojo.vo.worker.AddWorkerVo;
import com.example.Gym_management_system.pojo.vo.worker.SelWorkerByIdVo;
import com.example.Gym_management_system.pojo.vo.worker.WorkerListVo;
import com.example.Gym_management_system.utils.JWTLoginEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkerMapstruct {

    WorkerMapstruct INSTANCE = Mappers.getMapper(WorkerMapstruct.class);

    GetInfoVo dtogetInfoToVo(Worker worker);
    GetInfoVo dtogetInfoToCustomer(Customer worker);
    GetInfoVo dtogetInfoToTrainer(Trainer worker);

    JWTLoginEntity dtoWorkerToJWT(Worker worker);
    JWTLoginEntity dtoCustomerToJWT(Customer worker);
    JWTLoginEntity dtoTrainerToJWT(Trainer worker);

    Worker voWorkerToEn(AddWorkerVo addWorkerVo);

    //    @Mapping(target = "sex", source = "sex", qualifiedByName = "mapFieldToString")
    WorkerListVo dtoWorkerToListVo(Worker worker);

    Worker jwtWorkerToEn(JWTLoginEntity jwtLoginEntity);

    SelWorkerByIdVo dtoIdWorkerToVo(Worker worker);


//    @Named("mapFieldToString")
//    default String mapFieldToString(Boolean sex) {
//        if (sex != null && sex.equals(true)) {
//            return "男";
//        } else if (sex != null && sex.equals(false)) {
//            return "女";
//        }
//        return null;
//    }
}
