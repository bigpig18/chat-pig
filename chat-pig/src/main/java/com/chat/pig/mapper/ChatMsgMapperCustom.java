package com.chat.pig.mapper;

import com.chat.pig.entity.ChatMsg;
import com.chat.pig.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述: chatMsg表自定义数据库接口定义
 *
 * @author li
 * @date 2019/12/24
 */
@Component
public interface ChatMsgMapperCustom extends MyMapper<ChatMsg> {

    /**
     * 批量签收消息
     * @param msgIds 要签收的消息的id
     */
    void batchSignMsg(List<String> msgIds);
}