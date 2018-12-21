package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建人:连磊
 * 日期: 2018/12/21. 14:53
 * 描述：
 */
@RestController
public class FundController {

    @GetMapping("/getf")
    public void getFund(@RequestParam String userId){
        System.out.println(userId);
    }

}
