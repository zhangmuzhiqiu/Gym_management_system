package com.example.Gym_management_system.commons;

import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName: hi-store
 * @Titile: Constans
 * @Author: Lucky
 * @Description: 常量类
 */
public class Constants {
    //操作成功的响应码
    public final static Integer SUCCESS_CODE = 1000;
    //参数为空的响应码
    public final static Integer ARGS_EMPTY_CODE = 2001;
    //密码不一致
    public final static Integer PWD_ILLEGAL_CODE = 2002;
    //默认不删除
    public static final Integer IS_NOT_DELETE = 0;
    //被删除
    public static final Integer IS_DELETE = 1;
    //数据库发生异常的code
    public static final Integer ERROR_CODE = 2000;
    //请求头的key
    public static final String TOKEN_HEADER = "X-Token";
    //文件的类型
    public static final List<String> FILE_TYPES = Arrays.asList("image/png", "image/jpeg");
    //文件大小
    public static final long FILE_SIZE = 1024 * 1024 * 2;
    //默认头像地址
    public static final String AVATAR_ADDRESS = "images/avatar.png";
    //默认地址
    public final static int IS_DEFAULT_ADDRESS = 1;
    //非默认地址
    public final static int IS_NOT_DEFAULT_ADDRESS = 0;
    //员工默认密码
    public static final String WORKER_PASSWORD = "123456";
}
