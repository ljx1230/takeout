package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @Author: ljx
 * @Date: 2024/1/29 13:13
 */
public interface UserService {
    User wxlogin(UserLoginDTO userLoginDTO);
}
