package com.service.impl;

import com.common.ServerResponse;
import com.dao.UserMapper;
import com.pojo.User;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: EWeb
 * @description: 用户模块service实现
 * @author: zpp
 * @create: 2018-08-27 20:54
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount=userMapper.selectCountByName(username);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        User user=userMapper.selectLogin(username,password);
        if(user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }
}
