package com.chat.pig.controller;

import com.chat.pig.entity.Users;
import com.chat.pig.service.IUserService;
import com.chat.pig.utils.ResponseJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 用户服务对外相关接口
 *
 * @author li
 * @date 2019/12/25
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录或注册
     * @param users 用户信息
     * @return {@link ResponseJsonResult}
     * @throws Exception 异常
     */
    @PostMapping(value = "/registerOrLogin")
    public ResponseJsonResult registerOrLogin(@RequestBody Users users) throws Exception {
        return userService.userRegisterOrLogin(users);
    }

}
