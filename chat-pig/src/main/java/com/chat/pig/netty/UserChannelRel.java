package com.chat.pig.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: userId 与 channel 关联关系
 *
 * @author li
 * @date 2020/1/8
 */
public class UserChannelRel {

    private static Map<String, Channel> manager = new HashMap<>(16);

    public static void put(String userId,Channel channel){
        manager.put(userId,channel);
    }

    public static Channel getChannel(String userId){
        return manager.get(userId);
    }
}
