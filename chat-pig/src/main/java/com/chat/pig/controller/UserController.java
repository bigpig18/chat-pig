package com.chat.pig.controller;

import com.chat.pig.entity.Users;
import com.chat.pig.entity.bo.UsersBo;
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

    /**
     * 上传头像到文件服务器
     * @param usersBo {@link UsersBo}
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/uploadFaceImg")
    public ResponseJsonResult uploadFaceImg(@RequestBody UsersBo usersBo){
        return userService.uploadFaceImg(usersBo);
    }

    /**
     * 上传头像到文件服务器
     * @param usersBo {@link UsersBo}
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/modifyNickName")
    public ResponseJsonResult modifyNickName(@RequestBody UsersBo usersBo){
        return userService.modifyNickName(usersBo);
    }

    /**
     * 搜索好友,根据账号做匹配查询,非模糊查询
     * @param userId 用户本人id
     * @param friendUsername 搜索的好友名
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/findFriend")
    public ResponseJsonResult findFriend(String userId,String friendUsername){
        return userService.findFriend(userId,friendUsername);
    }
}
