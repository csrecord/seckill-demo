package com.chenhf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenhf.pojo.User;
import com.chenhf.vo.LoginVo;
import com.chenhf.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenhf
 */
public interface IUserService extends IService<User> {

    /**
     * @description 登录实现接口
     * @param loginVo
     * @param request
     * @param response
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 19:21
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * @description 根据user获取用户
     * @param userTicket
     * @return User
     * @author Chenhf
     * @date 2022/7/4 15:43
     */
    User getUserByCookie(String userTicket, HttpServletRequest httpServletRequest, HttpServletResponse response);

    /**
     * @description 更新密码
     * @param userTicket
     * @param password
     * @return RespBean
     * @author Chenhf
     * @date 2022/7/7 21:36
     */
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
