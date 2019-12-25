package com.chat.pig.service;

import com.chat.pig.entity.Users;
import com.chat.pig.utils.ResponseJsonResult;

/**
 * 描述: 用户服务接口相关定义
 *
 * @author li
 * @date 2019/12/25
 */
public interface IUserService {

    /**
     * 判断用户名是否存在
     * @param userName 用户名
     * @return boolean
     */
    boolean queryUsernameIsExist(String userName);

    /**
     * 用户登录或注册
     * @param users 用户信息
     * @return {@link ResponseJsonResult}
     * @throws Exception 异常
     */
    ResponseJsonResult userRegisterOrLogin(Users users) throws Exception;
}
