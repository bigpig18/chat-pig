package com.chat.pig.service.impl;

import com.chat.pig.entity.Users;
import com.chat.pig.entity.bo.UsersBo;
import com.chat.pig.entity.vo.UsersVo;
import com.chat.pig.mapper.UsersMapper;
import com.chat.pig.service.IUserService;
import com.chat.pig.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;

/**
 * 描述: 用户服务接口实现
 *
 * @author li
 * @date 2019/12/25
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UsersMapper usersMapper;

    private final FastDFSClient fastDFSClient;

    private final QRCodeUtils codeUtils;

    @Autowired
    public UserServiceImpl(UsersMapper usersMapper, FastDFSClient fastDFSClient, QRCodeUtils codeUtils) {
        this.usersMapper = usersMapper;
        this.fastDFSClient = fastDFSClient;
        this.codeUtils = codeUtils;
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public boolean queryUsernameIsExist(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        Users result = usersMapper.selectOne(user);
        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(Users users) {
        //根据用户主键，有值的更新，为空的不更新
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public ResponseJsonResult queryUserById(String id) {
        Users users = usersMapper.selectByPrimaryKey(id);
        UsersVo userVo = new UsersVo();
        BeanUtils.copyProperties(users,userVo);
        return ResponseJsonResult.ok(userVo);
    }

    @Override
    public ResponseJsonResult userRegisterOrLogin(Users users) throws Exception {
        //判断用户名和密码不能为空
        if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
            return ResponseJsonResult.errorMsg("用户名密码不能为空");
        }
        //用户名存在则登录，不存在则注册
        boolean userNameIsExist = queryUsernameIsExist(users.getUsername());
        Users userResult;
        if (userNameIsExist){
            //登录
            log.info("用户登录:{}",users.getUsername());
            userResult = queryUserForLogin(users.getUsername(),MD5Utils.getMd5Str(users.getPassword()));
            if (userResult == null){
                log.error("用户名登录失败:{}",users.getUsername());
                return ResponseJsonResult.errorMsg("用户名或密码不正确");
            }
        }else {
            //注册
            log.info("用户注册:{}",users.getUsername());
            userResult = registerUser(users);
        }
        UsersVo userVo = new UsersVo();
        BeanUtils.copyProperties(userResult,userVo);
        return ResponseJsonResult.ok(userVo);
    }

    @Override
    public ResponseJsonResult uploadFaceImg(UsersBo usersBo) {
        //临时存储路径
        String userFacePath = "classpath:tmp/" + usersBo.getId() + "userFace.png";
        //获取前端传来的base64字符串
        String base64Data = usersBo.getFaceData();
        //转换为文件对象
        try {
            FileUtils.base64ToFile(userFacePath,base64Data);
            MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
            //上传文件到fastdfs服务器，并返回图片url
            String bigImgUrl = fastDFSClient.uploadBase64(faceFile);
            log.info("User Face Image Url: {}",bigImgUrl);
            //获取缩略图url
            String[] arr = bigImgUrl.split("\\.");
            String thumpImgUrl = arr[0] + "_8080." + arr[1];
            //更新用户头像
            Users user = new Users();
            user.setId(usersBo.getId());
            user.setFaceImageBig(bigImgUrl);
            user.setFaceImage(thumpImgUrl);
            updateUserInfo(user);
        } catch (Exception e) {
            log.error("base64 To File Error..." + e.getMessage());
            return ResponseJsonResult.errorException("Upload Face Image Fail...");
        }
        return queryUserById(usersBo.getId());
    }

    @Override
    public ResponseJsonResult modifyNickName(UsersBo usersBo) {
        if (StringUtils.isBlank(usersBo.getId()) || StringUtils.isBlank(usersBo.getNickName())){
            return ResponseJsonResult.errorMsg("昵称不能为空");
        }
        if (usersBo.getNickName().length() < 6 || usersBo.getNickName().length() > 12){
            return ResponseJsonResult.errorMsg("昵称长度不能大于6位或小于12位");
        }
        Users users = new Users();
        users.setId(usersBo.getId());
        users.setNickname(usersBo.getNickName());
        updateUserInfo(users);
        return queryUserById(usersBo.getId());
    }

    /**
     * 用户登录
     * @param userName 用户名
     * @param pwd 密码
     * @return {@link Users}
     */
    private Users queryUserForLogin(String userName, String pwd) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",userName);
        criteria.andEqualTo("password",pwd);
        //根据条件查询
        return usersMapper.selectOneByExample(userExample);
    }

    /**
     * 用户注册
     * @param users 注册信息
     * @return {@link Users}
     */
    private Users registerUser(Users users) {
        String userId = GenerateUniqueId.generate16Id();
        //刚注册的用户直接用userName，后续可修改昵称
        users.setNickname(users.getUsername());
        //用户头像
        users.setFaceImage(null);
        users.setFaceImageBig(null);
        try {
            users.setPassword(MD5Utils.getMd5Str(users.getPassword()));
        } catch (Exception e) {
            log.error("Pwd To Md5 Error...{}",e.getMessage());
            return null;
        }
        //生成唯一二维码
        //pigChat_qrCode:[username]
        String qrCodePath = "classpath:tmp/user_" + userId + "_qrCode.png";
        codeUtils.createQRCode(qrCodePath,"pigChat_qrCode:[" + users.getUsername() + "]");
        MultipartFile codeFile = FileUtils.fileToMultipart(qrCodePath);
        String codeUrl;
        try {
            codeUrl = fastDFSClient.uploadQRCode(codeFile);
        } catch (IOException e) {
            log.error("Upload QrCode Error...{}",e.getMessage());
            return null;
        }
        users.setQrcode(codeUrl);
        //插入唯一主键
        users.setId(userId);
        usersMapper.insert(users);
        return users;
    }
}
