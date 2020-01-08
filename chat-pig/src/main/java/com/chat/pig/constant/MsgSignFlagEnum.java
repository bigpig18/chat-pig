package com.chat.pig.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 消息签收状态 枚举
 *
 * @author li
 * @date 2019/12/31
 */
@Getter
@AllArgsConstructor
public enum MsgSignFlagEnum {

	/**
	 * 0 - 未签收
	 * 1 - 已签收
	 */
	UNSIGN(0, "未签收"),
	SIGNED(1, "已签收");
	
	public final Integer type;
	public final String content;
}
