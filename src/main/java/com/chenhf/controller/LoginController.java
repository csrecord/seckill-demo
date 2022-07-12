package com.chenhf.controller;

import com.chenhf.service.IUserService;
import com.chenhf.vo.LoginVo;
import com.chenhf.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @description: 登录跳转controller
 * @className: LoginController
 * @author: Chenhf
 * @date: 2022/6/30 16:19
 * @version: 1.0
 */
//@RestController
// @RestController会把返回值只给返回给httpResponse,需要用Controller
//页面跳转不能使用RestController
@Controller
@RequestMapping("/login")
//输出日志的
@Slf4j
@SuppressWarnings("all")
public class LoginController {

    //Controller调用Service, 使用Autowired注入
    @Autowired
    private IUserService userService;

    /**
     * @description 跳转登录页
     * @param
     * @return String
     * @author Chenhf
     * @date 2022/6/30 16:21
     */
    //去到登录页,登录页面为localhost:8080/login/toLogin
    @RequestMapping("/toLogin")
    public String toLogin(){
        //跳转到login.html
        return "login";
    }

    /**
     * @description 登录跳转实现
     * @param loginVo
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 19:03
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    //加入validation校验功能
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        //用来测试用户输入
        //log.info("{}",loginVo);
        //Controller调用service
        return userService.doLogin(loginVo, request, response);
    }
}
