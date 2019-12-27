package com.chat.pig.service;

import com.chat.pig.entity.Users;
import com.chat.pig.entity.bo.UsersBo;
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
     * 修改用户记录
     * @param users 用户信息
     */
    void updateUserInfo(Users users);

    /**
     * 根据用户id查询用户信息
     * @param id 用户id
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult queryUserById(String id);

    /**
     * 用户登录或注册
     * @param users 用户信息
     * @return {@link ResponseJsonResult}
     * @throws Exception 异常
     */
    ResponseJsonResult userRegisterOrLogin(Users users) throws Exception;

    /**
     * 上传头像到文件服务器
     * @param usersBo {@link UsersBo}
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult uploadFaceImg(UsersBo usersBo);

    /**
     * 修改用户昵称
     * @param usersBo {@link UsersBo}
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult modifyNickName(UsersBo usersBo);
}
