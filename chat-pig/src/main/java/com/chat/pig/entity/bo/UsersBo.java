package com.chat.pig.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 前端传来的用户信息
 *
 * @author li
 * @date 2019/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersBo {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户头像信息
     */
    private String faceData;
    /**
     * 用户昵称
     */
    private String nickName;
}
