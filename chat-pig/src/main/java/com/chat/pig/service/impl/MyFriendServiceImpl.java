package com.chat.pig.service.impl;

import com.chat.pig.constant.SearchFriendsStatusEnum;
import com.chat.pig.entity.MyFriends;
import com.chat.pig.entity.vo.UsersVo;
import com.chat.pig.mapper.MyFriendsMapper;
import com.chat.pig.service.IMyFriendService;
import com.chat.pig.service.IUserService;
import com.chat.pig.utils.ResponseJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * 描述: 我的好友服务相关接口实现
 *
 * @author li
 * @date 2019/12/31
 */
@Slf4j
@Service
public class MyFriendServiceImpl implements IMyFriendService {

    private final MyFriendsMapper friendsMapper;

    private final IUserService userService;

    @Autowired
    public MyFriendServiceImpl(MyFriendsMapper friendsMapper, IUserService userService) {
        this.friendsMapper = friendsMapper;
        this.userService = userService;
    }

    @Override
    public ResponseJsonResult findFriend(String userId, String friendUsername) {
        //判断传入参数是否为空
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUsername)){
            return ResponseJsonResult.errorMsg("");
        }
        //前置条件
        Integer status = preconditionFindFriend(userId, friendUsername);
        if (!status.equals(SearchFriendsStatusEnum.SUCCESS.status)){
            return ResponseJsonResult.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }
        return userService.queryUserByUsername(friendUsername);
    }

    @Override
    public ResponseJsonResult addFriend(String userId, String friendUsername) {
        return null;
    }

    /**
     * 查询好友的前置条件
     * @param userId 用户id
     * @param friendUsername 搜索的好友的用户名
     * @return {@link SearchFriendsStatusEnum}.status
     */
    private Integer preconditionFindFriend(String userId, String friendUsername){
        UsersVo user = (UsersVo) userService.queryUserByUsername(friendUsername).getData();
        if (user == null){
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }else if (user.getId().equals(userId)){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        Example userFriends = new Example(MyFriends.class);
        Example.Criteria criteria = userFriends.createCriteria();
        criteria.andEqualTo("myUserId",userId);
        criteria.andEqualTo("myFriendUserId",user.getId());
        MyFriends myFriends = friendsMapper.selectOneByExample(userFriends);
        if (myFriends != null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }
}