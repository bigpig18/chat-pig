package com.chat.pig.mapper;

import com.chat.pig.entity.FriendsRequest;
import com.chat.pig.entity.vo.FriendRequestVo;
import com.chat.pig.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述: users表自定义操作数据库接口
 *
 * @author li
 * @date 2019/12/24
 */
@Component
public interface FriendsRequestMapperCustom extends MyMapper<FriendsRequest> {

    /**
     * 查询好友请求列表
     * @param accepterId 接收这id
     * @return {@link FriendRequestVo}s
     */
    List<FriendRequestVo> queryFriendRequestList(String accepterId);
}