package com.chat.pig.mapper;

import com.chat.pig.entity.MyFriends;
import com.chat.pig.entity.vo.MyFriendsVo;
import com.chat.pig.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述: myFriends表数据库自定义接口定义
 *
 * @author li
 * @date 2019/12/24
 */
@Component
public interface MyFriendsMapperCustom extends MyMapper<MyFriends> {

    /**
     * 获取用户所有好友信息
     * @param userId 用户id
     * @return {@link MyFriendsVo}s
     */
    List<MyFriendsVo> getAllUserFriends(String userId);
}