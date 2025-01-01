package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriBuilder;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/1 22:24
 */
@Service
public class UserServiceImpl implements UserService {

    private final static String OPENID = "openid";
    private final static String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WeChatProperties weChatProperties;

    @Autowired
    UserMapper userMapper;


    @Override
    @Transactional
    public User login(UserLoginDTO userLoginDTO) throws IOException, URISyntaxException {
        // 创建服务端
        CloseableHttpClient client = HttpClientBuilder.create().build();

        // 创建URI对象
        URIBuilder builder = new URIBuilder(WX_LOGIN);
        builder.addParameter("appId", weChatProperties.getAppid());
        builder.addParameter("secret", weChatProperties.getSecret());
        builder.addParameter("code", userLoginDTO.getCode());
        builder.addParameter("grant_type", "authorization_code");
        URI uri = builder.build();

        // 创建GET请求
        HttpGet get = new HttpGet(uri);

        // 发送请求
        CloseableHttpResponse response = client.execute(get);

        // 获取响应体
        HttpEntity responseEntity = response.getEntity();

        // 解析JSON
        String s = EntityUtils.toString(responseEntity);
        JSONObject object = JSONObject.parseObject(s);
        String openId = (String) object.get("OPENID");

        if (openId != null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断是否已经注册
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            // 未注册则插入新用户数据
            User newUser = new User();
            newUser.setOpenid(openId);
            newUser.setCreateTime(LocalDateTime.now());
            userMapper.insert(newUser);
            return newUser;
        }

        return user;
    }

}
