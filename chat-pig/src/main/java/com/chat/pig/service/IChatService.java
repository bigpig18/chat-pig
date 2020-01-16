package com.chat.pig.service;

import com.chat.pig.netty.ChatMsg;
import com.chat.pig.utils.ResponseJsonResult;

import java.util.List;

/**
 * 描述: 聊天相关服务接口定义
 *
 * @author li
 * @date 2020/1/8
 */
public interface IChatService {

    /**
     * 保存聊天消息到数据库
     * @param chatMsg {@link ChatMsg}
     * @return 消息记录id
     */
    String saveMsg(ChatMsg chatMsg);

    /**
     * 签收消息
     * @param msgIds 要签收的消息id
     */
    void signMsg(List<String> msgIds);

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptId 接收方id
     * @return {@link ResponseJsonResult}
     */
    ResponseJsonResult getUnsignedMsgList(String acceptId);
}
