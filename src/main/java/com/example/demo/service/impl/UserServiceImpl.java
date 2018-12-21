package com.example.demo.service.impl;

import com.example.demo.bean.UserEntity;
import com.example.demo.service.UserService;
import com.example.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 创建人:连磊
 * 日期: 2018/12/21. 14:59
 * 描述：
 * @author Tony
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${jwt.base64Encryption}")
    private String base64Encryption;

    @Override
    public String login(UserEntity userEntity) {
        if("root".equals(userEntity.getName()) && "root".equals(userEntity.getPwd())) {
            return JWTUtil.getUserToken(userEntity.getName() , userEntity.getPwd() , Long.valueOf(1000*60*10), base64Encryption);
        } else {
            return "用户名或密码错误!";
        }
    }
}
