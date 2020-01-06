package com.chat.pig.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 返回给前端的用户好友对象
 *
 * @author li
 * @date 2019/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFriendsVo {

    /**
     * 好友的id
     */
    private String friendId;
    /**
     * 好友的用户名，账号
     */
    private String friendUsername;
    /**
     * 好友头像
     */
    private String friendFaceImage;
    /**
     * 好友昵称
     */
    private String friendNickname;
}
