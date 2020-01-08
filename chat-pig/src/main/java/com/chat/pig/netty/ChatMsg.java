package com.chat.pig.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 描述: 聊天实体对象模型
 *
 * @author li
 * @date 2020/1/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = -2945278060064336625L;
    /**
     * 聊天内容
     */
    private String msg;
    /**
     * 发送者id
     */
    private String sendId;
    /**
     * 接收者id
     */
    private String acceptId;
    /**
     * 用于消息的签收
     */
    private String msgId;
}
