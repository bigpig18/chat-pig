package com.chat.pig.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 添加好友前置状态 枚举
 *
 * @author li
 * @date 2019/12/31
 */
@Getter
@AllArgsConstructor
public enum SearchFriendsStatusEnum {

	/**
	 * 0 - 好友可添加
	 */
	SUCCESS(0, "OK"),
	USER_NOT_EXIST(1, "无此用户"),
	NOT_YOURSELF(2, "不能添加你自己"),
	ALREADY_FRIENDS(3, "该用户已经是你的好友");
	
	public final Integer status;
	public final String msg;

	public static String getMsgByKey(Integer status) {
	    Objects.requireNonNull(status);
        return Stream.of(values())
                .filter(e -> e.status.equals(status))
                .findAny()
                .map(SearchFriendsStatusEnum::getMsg)
                .orElse(null);
	}
	
}
