package com.example.Gym_management_system.pojo.vo.equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SelEquipmentVo {

    private Integer pageIndex;
    private String uid;
    private Integer location;
    private Boolean state;
    private String name;
    private LocalDate create_time;
    private Boolean type;
}
