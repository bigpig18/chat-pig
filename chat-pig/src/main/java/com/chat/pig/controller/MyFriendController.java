package com.chat.pig.controller;

import com.chat.pig.service.IMyFriendService;
import com.chat.pig.utils.ResponseJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 我的好友操作相关对外接口
 *
 * @author li
 * @date 2019/12/31
 */
@RequestMapping(value = "/friend")
@RestController
public class MyFriendController {

    private final IMyFriendService myFriendService;

    @Autowired
    public MyFriendController(IMyFriendService myFriendService) {
        this.myFriendService = myFriendService;
    }

    /**
     * 搜索好友,根据账号做匹配查询,非模糊查询
     * @param userId 用户本人id
     * @param friendUsername 搜索的好友账号
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/findFriend")
    public ResponseJsonResult findFriend(String userId, String friendUsername){
        return myFriendService.findFriend(userId,friendUsername);
    }

    /**
     * 发送添加好友的请求
     * @param userId 用户本人id
     * @param friendUsername 搜索的好友账号
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/addFriend")
    public ResponseJsonResult addFriend(String userId,String friendUsername){
        return myFriendService.addFriend(userId,friendUsername);
    }

    /**
     * 查询好友请求列表
     * @param acceptId 接收者id
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/queryFriendRequest")
    public ResponseJsonResult queryFriendRequest(String acceptId){
        return myFriendService.queryFriendRequest(acceptId);
    }

    /**
     * 接收方通过或者忽略好友请求
     * @param acceptId 接受方id
     * @param sendId 发送方id
     * @param operatorType 操作类型
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/operatorFriendRequest")
    public ResponseJsonResult operatorFriendRequest(String acceptId,String sendId,Integer operatorType){
        return myFriendService.operatorFriendRequest(acceptId,sendId,operatorType);
    }

    /**
     * 查询用户所有的好友
     * @param userId 用户id
     * @return {@link ResponseJsonResult}
     */
    @GetMapping(value = "/allMyFriends")
    public ResponseJsonResult allMyFriends(String userId){
        return myFriendService.queryAllUserFriends(userId);
    }
}
