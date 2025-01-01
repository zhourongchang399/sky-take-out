package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public interface UserService {
    User login(UserLoginDTO userLoginDTO) throws IOException, URISyntaxException;
}
