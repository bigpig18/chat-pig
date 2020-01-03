package com.chat.pig.service;

import com.chat.pig.utils.ResponseJsonResult;

/**
 * 描述: 我的好友服务接口相关定义
 *
 * @author li
 * @date 2019/12/31
 */
public interface IMyFriendService {

    /**
     * 搜索好友,根据账号做匹配查询,非模糊查询
     * @param userId 用户本人id
     * @param friendUsername 搜索的好友账号
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult findFriend(String userId, String friendUsername);

    /**
     * 发送添加好友的请求
     * @param userId 用户本人id
     * @param friendUsername 搜索的好友账号
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult addFriend(String userId, String friendUsername);

    /**
     * 查询好友请求列表
     * @param acceptId 接收者id
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult queryFriendRequest(String acceptId);
}
