package com.chat.pig.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 返回给前端的Users对象
 *
 * @author li
 * @date 2019/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersVo {

    /**
     * id
     */
    private String id;
    /**
     * 用户名，账号
     */
    private String username;
    /**
     * 我的头像，如果没有默认给一张
     */
    private String faceImage;
    private String faceImageBig;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;
}
