package com.chat.pig.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 描述: 构建消息模型对象
 *
 * @author li
 * @date 2020/1/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataContent implements Serializable {

    private static final long serialVersionUID = 7585962170133129742L;
    /**
     * 动作类型
     */
    private Integer action;
    /**
     * 用户聊天内容
     */
    private ChatMsg chatMsg;
    /**
     * 扩展字段
     */
    private String extend;
}
