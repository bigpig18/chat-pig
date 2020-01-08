package com.chat.pig.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 发送消息的动作 枚举
 *
 * @author li
 * @date 2019/12/31
 */
@Getter
@AllArgsConstructor
public enum MsgActionEnum {

	/**
	 *
	 */
	CONNECT(1, "第一次(或重连)初始化连接"),
	CHAT(2, "聊天消息"),	
	SIGNED(3, "消息签收"),
	KEEPALIVE(4, "客户端保持心跳"),
	PULL_FRIEND(5, "拉取好友");
	
	public final Integer type;
	public final String content;

}
