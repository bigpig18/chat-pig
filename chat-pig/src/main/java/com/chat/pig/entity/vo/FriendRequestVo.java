package com.chat.pig.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 返回给前端的好友请求发送者信息
 *
 * @author li
 * @date 2019/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestVo {

    /**
     *  发送者id
     */
    private String sendUserId;
    /**
     *  发送者账号
     */
    private String sendUsername;
    /**
     *  发送者头像
     */
    private String sendFaceImage;
    /**
     *  发送者昵称
     */
    private String sendNickname;
}
