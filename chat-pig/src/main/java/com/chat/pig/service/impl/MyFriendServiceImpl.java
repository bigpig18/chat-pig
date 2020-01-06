package com.chat.pig.service.impl;

import com.chat.pig.constant.OperatorFriendRequestTypeEnum;
import com.chat.pig.constant.SearchFriendsStatusEnum;
import com.chat.pig.entity.FriendsRequest;
import com.chat.pig.entity.MyFriends;
import com.chat.pig.entity.vo.MyFriendsVo;
import com.chat.pig.entity.vo.UsersVo;
import com.chat.pig.mapper.FriendsRequestMapper;
import com.chat.pig.mapper.FriendsRequestMapperCustom;
import com.chat.pig.mapper.MyFriendsMapper;
import com.chat.pig.mapper.MyFriendsMapperCustom;
import com.chat.pig.service.IMyFriendService;
import com.chat.pig.service.IUserService;
import com.chat.pig.utils.GenerateUniqueId;
import com.chat.pig.utils.ResponseJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

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

    private final MyFriendsMapperCustom friendsMapperCustom;

    private final FriendsRequestMapperCustom requestMapperCustom;

    private final FriendsRequestMapper requestMapper;

    private final IUserService userService;

    @Autowired
    public MyFriendServiceImpl(MyFriendsMapper friendsMapper, MyFriendsMapperCustom friendsMapperCustom, FriendsRequestMapperCustom requestMapperCustom, FriendsRequestMapper requestMapper, IUserService userService) {
        this.friendsMapper = friendsMapper;
        this.friendsMapperCustom = friendsMapperCustom;
        this.requestMapperCustom = requestMapperCustom;
        this.requestMapper = requestMapper;
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
        //判断传入参数是否为空
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(friendUsername)){
            return ResponseJsonResult.errorMsg("");
        }
        //前置条件
        Integer status = preconditionFindFriend(userId, friendUsername);
        if (!status.equals(SearchFriendsStatusEnum.SUCCESS.status)){
            return ResponseJsonResult.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }
        //发送添加好友请求
        sendFriendRequest(userId,friendUsername);
        return ResponseJsonResult.ok();
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public ResponseJsonResult queryFriendRequest(String acceptId) {
        if (StringUtils.isBlank(acceptId)){
            return ResponseJsonResult.errorMsg("");
        }
        return ResponseJsonResult.ok(requestMapperCustom.queryFriendRequestList(acceptId));
    }

    @Override
    public ResponseJsonResult operatorFriendRequest(String acceptId, String sendId, Integer operatorType) {
        //判断参数是否为空
        if (StringUtils.isBlank(acceptId) || StringUtils.isBlank(sendId) || operatorType == null){
            return ResponseJsonResult.errorMsg("");
        }

        if (operatorType.equals(OperatorFriendRequestTypeEnum.IGNORE.type)){
            //若为忽略，直接删除好友请求
            deleteFriendRequest(sendId,acceptId);
            return ResponseJsonResult.ok("已忽略");
        }else if (operatorType.equals(OperatorFriendRequestTypeEnum.PASS.type)){
            //若为通过，互相添加好友记录到数据库对应的表，而后删除好友请求
            saveFriendRequest(sendId, acceptId);
            saveFriendRequest(acceptId, sendId);
            deleteFriendRequest(sendId,acceptId);
            //返回更新后的好友列表，便于前端更新
            return ResponseJsonResult.ok(friendsMapperCustom.getAllUserFriends(acceptId));
        }else {
            //若操作类型无对应的枚举值，则返回空错误信息
            return ResponseJsonResult.errorMsg("");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public ResponseJsonResult queryAllUserFriends(String userId) {
        if (StringUtils.isBlank(userId)){
            return ResponseJsonResult.errorMsg("");
        }
        //查询好友列表
        List<MyFriendsVo> allMyFriends = friendsMapperCustom.getAllUserFriends(userId);
        return ResponseJsonResult.ok(allMyFriends);
    }

    /**
     * 发送好友请求
     * @param userId 用户id
     * @param friendUsername 要添加好友的账号
     */
    private void sendFriendRequest(String userId, String friendUsername) {
        //根据friendUsername获取朋友信息
        UsersVo friend = (UsersVo) userService.queryUserByUsername(friendUsername).getData();
        //查询发送好友请求记录表
        Example fre = new Example(FriendsRequest.class);
        Example.Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId",userId);
        frc.andEqualTo("acceptUserId",friend.getId());
        FriendsRequest friendsRequest = requestMapper.selectOneByExample(fre);
        if (friendsRequest == null){
            //生成唯一id
            String requestId = GenerateUniqueId.generate16Id();
            FriendsRequest request = new FriendsRequest(requestId,userId,friend.getId(),new Date());
            requestMapper.insert(request);
        }
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

    /**
     * 保存好友
     * @param sendId 发送者id
     * @param acceptId 接收者id
     */
    private void saveFriendRequest(String sendId, String acceptId){
        String id = GenerateUniqueId.generate16Id();
        MyFriends myFriends = new MyFriends(id,acceptId,sendId);
        friendsMapper.insert(myFriends);
    }

    /**
     * 删除好友请求记录
     * @param sendId 发送者id
     * @param acceptId 接收者id
     */
    private void deleteFriendRequest(String sendId,String acceptId){
        Example fre = new Example(FriendsRequest.class);
        Example.Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId",sendId);
        frc.andEqualTo("acceptUserId",acceptId);
        requestMapper.deleteByExample(fre);
    }
}
