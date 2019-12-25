package com.chat.pig.service.impl;

import com.chat.pig.entity.Users;
import com.chat.pig.entity.vo.UsersVo;
import com.chat.pig.mapper.UsersMapper;
import com.chat.pig.service.IUserService;
import com.chat.pig.utils.GenerateUniqueId;
import com.chat.pig.utils.MD5Utils;
import com.chat.pig.utils.ResponseJsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 描述: 用户服务接口实现
 *
 * @author li
 * @date 2019/12/25
 */
@Service
public class UserServiceImpl implements IUserService {

    private final UsersMapper usersMapper;

    @Autowired
    public UserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public boolean queryUsernameIsExist(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        Users result = usersMapper.selectOne(user);
        return result != null;
    }

    @Override
    public ResponseJsonResult userRegisterOrLogin(Users users) throws Exception {
        //判断用户名和密码不能为空
        if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
            return ResponseJsonResult.errorMsg("用户名密码不能为空");
        }
        //用户名存在则登录，不存在则注册
        boolean userNameIsExist = queryUsernameIsExist(users.getUsername());
        Users userResult = null;
        if (userNameIsExist){
            //登录
            userResult = queryUserForLogin(users.getUsername(),MD5Utils.getMd5Str(users.getPassword()));
            if (userResult == null){
                return ResponseJsonResult.errorMsg("用户名或密码不正确");
            }
        }else {
            //注册
            userResult = registerUser(users);
        }
        UsersVo userVo = new UsersVo();
        BeanUtils.copyProperties(userResult,userVo);
        return ResponseJsonResult.ok(userVo);
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
        criteria.andEqualTo("userName",userName);
        criteria.andEqualTo("password",pwd);
        //根据条件查询
        return usersMapper.selectOneByExample(userExample);
    }

    /**
     * 用户注册
     * @param users 注册信息
     * @return {@link Users}
     * @throws Exception 抛出异常
     */
    private Users registerUser(Users users) throws Exception {
        //刚注册的用户直接用userName，后续可修改昵称
        users.setNickname(users.getUsername());
        //用户头像
        users.setFaceImage(null);
        users.setFaceImageBig(null);
        users.setPassword(MD5Utils.getMd5Str(users.getPassword()));
        //生成唯一二维码
        users.setQrcode(null);
        //插入唯一主键
        users.setId(GenerateUniqueId.generate16Id());
        usersMapper.insert(users);
        return users;
    }
}
