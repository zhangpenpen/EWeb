package com.service.impl;

import com.common.Const;
import com.common.ServerResponse;
import com.common.TokenCache;
import com.dao.UserMapper;
import com.pojo.User;
import com.service.IUserService;
import com.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        int resultCount = userMapper.selectCountByName(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String Md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, Md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功", user);
    }

    public ServerResponse<String> register(User user) {
        ServerResponse serverResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        serverResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("email已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.selectCountByName(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.selectCountByEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    public ServerResponse selectQuestion(String username) {
        ServerResponse serverResponse = this.checkValid(username, Const.USERNAME);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestion(username);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("密码找回问题为空");
    }

    public ServerResponse checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案不正确");
    }

    public ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，传去正确token");
        }
        ServerResponse serverResponse = this.checkValid(username, Const.USERNAME);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或已过期");
        }
        if (org.apache.commons.lang3.StringUtils.equals(forgetToken, token)) {
            String Md5PasswordNew = MD5Util.MD5EncodeUtf8(password);
            int resultCount = userMapper.updatePasswordByUsername(username, Md5PasswordNew);
            if (resultCount > 0) {
                return ServerResponse.createBySuccess("密码修改完成");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，重新获取token");
        }
        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("密码修改成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmail(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换新的email再修改");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer id){
        User user=userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        return ServerResponse.createBySuccess(user);
    }
}
