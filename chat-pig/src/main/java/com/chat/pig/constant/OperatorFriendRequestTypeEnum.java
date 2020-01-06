package com.chat.pig.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 忽略或者通过 好友请求的枚举
 *
 * @author li
 * @date 2019/12/31
 */
@Getter
@AllArgsConstructor
public enum OperatorFriendRequestTypeEnum {

	/**
	 * 0 - 忽略，1 - 通过
	 */
	IGNORE(0, "忽略"),
	PASS(1, "通过");
	
	public final Integer type;
	public final String msg;

	public static String getMsgByType(Integer type) {
        Objects.requireNonNull(type);
		return Stream.of(values())
                .filter(e -> e.type.equals(type))
                .findAny()
                .map(OperatorFriendRequestTypeEnum::getMsg)
                .orElse(null);
	}
	
}
