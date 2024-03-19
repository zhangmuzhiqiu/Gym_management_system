package com.example.Gym_management_system.commons.result;


import com.example.Gym_management_system.commons.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @ProjectName: hi-store
 * @Titile: R  result
 * @Author: Lucky
 * @Description: 返回结果的统一封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    //响应码
    private Integer code;
    //需要信息
    private String msg;
    //需要数据
    private HashMap<String, Object> data;

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 成功数据
     *
     * @return
     */
    public static R ok() {
        return new R(Constants.SUCCESS_CODE, "OK");
    }

    public static R ok(String msg) {
        return new R(Constants.SUCCESS_CODE, msg);
    }

    public R data(String key, Object data) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, data);
        return this;
    }

}
