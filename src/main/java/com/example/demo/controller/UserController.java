package com.example.demo.controller;

import com.example.demo.bean.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 创建人:连磊
 * 日期: 2018/12/21. 10:07
 * 描述：
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public String login(@ModelAttribute UserEntity userEntity) {
        String login = userService.login(userEntity);
        return login;
    }

}
