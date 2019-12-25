package com.chat.pig.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 描述: 用户信息
 *
 * @author li
 * @date 2019/12/24
 */
@Data
public class Users {
    @Id
    private String id;

    /**
     * 用户名，账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 我的头像，如果没有默认给一张
     */
    @Column(name = "face_image")
    private String faceImage;

    @Column(name = "face_image_big")
    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;

    private String cid;
}