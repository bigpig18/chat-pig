package com.chat.pig.service.impl;

import com.chat.pig.constant.MsgSignFlagEnum;
import com.chat.pig.mapper.ChatMsgMapper;
import com.chat.pig.mapper.ChatMsgMapperCustom;
import com.chat.pig.netty.ChatMsg;
import com.chat.pig.service.IChatService;
import com.chat.pig.utils.GenerateUniqueId;
import com.chat.pig.utils.ResponseJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 描述: 聊天相关服务接口定义
 *
 * @author li
 * @date 2020/1/8
 */
@Slf4j
@Service
public class ChatServiceImpl implements IChatService {

    private final ChatMsgMapper chatMsgMapper;

    private final ChatMsgMapperCustom chatMsgMapperCustom;

    @Autowired
    public ChatServiceImpl(ChatMsgMapper chatMsgMapper, ChatMsgMapperCustom chatMsgMapperCustom) {
        this.chatMsgMapper = chatMsgMapper;
        this.chatMsgMapperCustom = chatMsgMapperCustom;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public String saveMsg(ChatMsg chatMsg) {
        com.chat.pig.entity.ChatMsg msgDb = new com.chat.pig.entity.ChatMsg();
        String msgId = GenerateUniqueId.generate16Id();
        msgDb.setId(msgId);
        msgDb.setAcceptUserId(chatMsg.getAcceptId());
        msgDb.setSendUserId(chatMsg.getSendId());
        msgDb.setCreateTime(new Date());
        msgDb.setSignFlag(MsgSignFlagEnum.UNSIGN.type);
        msgDb.setMsg(chatMsg.getMsg());
        log.info("Save Msg-{} - User-{} To Accept-{}",chatMsg.getMsg(),chatMsg.getSendId(),chatMsg.getAcceptId());
        chatMsgMapper.insert(msgDb);
        return msgId;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public void signMsg(List<String> msgIds) {
        if (msgIds != null && !msgIds.isEmpty()){
            chatMsgMapperCustom.batchSignMsg(msgIds);
        }
        log.error("Sign Msg: MsgIds is Empty");
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public ResponseJsonResult getUnsignedMsgList(String acceptId) {
        Example chatExample = new Example(com.chat.pig.entity.ChatMsg.class);
        Example.Criteria criteria = chatExample.createCriteria();
        criteria.andEqualTo("acceptUserId",acceptId);
        criteria.andEqualTo("signFlag",0);
        List<com.chat.pig.entity.ChatMsg> unsignedMsgList = chatMsgMapper.selectByExample(chatExample);
        return ResponseJsonResult.ok(unsignedMsgList);
    }
}
