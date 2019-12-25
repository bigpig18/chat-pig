package com.chat.pig.mapper;

import com.chat.pig.entity.ChatMsg;
import com.chat.pig.utils.MyMapper;
import org.springframework.stereotype.Component;

/**
 * 描述: chatMsg表数据库接口定义
 *
 * @author li
 * @date 2019/12/24
 */
@Component
public interface ChatMsgMapper extends MyMapper<ChatMsg> {
}