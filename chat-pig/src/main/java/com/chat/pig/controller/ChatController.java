package com.chat.pig.controller;

import com.chat.pig.service.IChatService;
import com.chat.pig.utils.ResponseJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 聊天信息相关对外接口
 *
 * @author li
 * @date 2020/1/16
 */
@RequestMapping(value = "/chat")
@RestController
public class ChatController {

    private final IChatService chatService;

    @Autowired
    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 用户手机端获取未签收的消息列表
     * @param acceptId 接收方id
     * @return {@link ResponseJsonResult}
     */
    @PostMapping(value = "/getUnsignedMsgList")
    public ResponseJsonResult getUnsignedMsgList(String acceptId){
        return chatService.getUnsignedMsgList(acceptId);
    }

}
