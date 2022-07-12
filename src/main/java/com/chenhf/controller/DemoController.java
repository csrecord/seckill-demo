package com.chenhf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 配置Thymeleaf之后,测试SpringBoot项目
 * @className: DemoController
 * @author: Chenhf
 * @date: 2022/6/29 16:11
 * @version: 1.0
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    /**
     * @description 返回model中的值,提供给hello.html页面
     *              测试路径localhost:8080/demo/hello
     * @param model
     * @return String
     * @author Chenhf
     * @date 2022/6/29 16:30
     */
    //映射到hello.html
    @RequestMapping("/hello")
    public String hello(Model model){
        //可以在thymeleaf中活动name的值,返回给html, Thymeleaf通过${name}获取value
        model.addAttribute("name", "chenhf");
        return "hello";
    }
}
