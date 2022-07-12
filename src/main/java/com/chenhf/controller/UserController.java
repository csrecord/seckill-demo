package com.chenhf.controller;


import com.chenhf.pojo.User;
import com.chenhf.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenhf
 */
@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * @description 用来测试用户信息
     * @param user
     * @return RespBean
     * @author Chenhf
     * @date 2022/7/7 15:58
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

}
