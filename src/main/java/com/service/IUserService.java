package com.service;

import com.common.ServerResponse;
import com.pojo.User;

public interface IUserService {
    public ServerResponse<User> login(String username, String password);
}
