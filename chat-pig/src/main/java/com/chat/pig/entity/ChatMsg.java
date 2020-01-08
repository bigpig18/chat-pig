package com.chat.pig.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 描述: 聊天记录
 *
 * @author li
 * @date 2019/12/24
 */
@Data
@Table(name = "chat_msg")
public class ChatMsg {
    @Id
    private String id;

    @Column(name = "send_user_id")
    private String sendUserId;

    @Column(name = "accept_user_id")
    private String acceptUserId;

    private String msg;

    /**
     * 消息是否签收状态
     * 1：签收
     * 0：未签收
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 发送请求的时间
     */
    @Column(name = "create_time")
    private Date createTime;
}