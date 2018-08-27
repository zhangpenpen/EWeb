package com.controller.protal;

import com.common.Const;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: EWeb
 * @description: 用户登陆
 * @author: zpp
 * @create: 2018-08-27 20:42
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iuserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iuserService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getDate());
        }
        return response;
    }
}
