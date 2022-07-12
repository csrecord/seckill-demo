package com.chenhf.service.serviceImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenhf.exception.GlobalException;
import com.chenhf.mapper.UserMapper;
import com.chenhf.pojo.User;
import com.chenhf.service.IUserService;
import com.chenhf.utils.CookieUtil;
import com.chenhf.utils.MD5Util;
import com.chenhf.utils.UUIDUtil;
import com.chenhf.vo.LoginVo;
import com.chenhf.vo.RespBean;
import com.chenhf.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 *  Service层处理业务逻辑数据
 * </p>
 *
 * @author chenhf
 */
@SuppressWarnings("all")
//service层
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //Service调用Mapper
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @description 登录接口实现类, 处理登录业务逻辑, 包含参数校验等功能
     * @param loginVo
     * @param request
     * @param response
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 19:22
     */

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //获取user
        User user = userMapper.selectById(mobile);
        if (user == null){
            //改造返回值, 异常处理的方式
            //定义完异常之后这样放进去
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //如果用户密码不正确
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        //使用redis存储用户信息
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket, user);
        //key就是cookie,值就是user对象
        //request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    /**
     * @description getUserByCookie
     * @param userTicket
     * @return User
     * @author Chenhf
     * @date 2022/7/4 15:44
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)){
            return null;
        }
        //当时为了解决分布式情况下用户不一致的问题,缓存的是user对象,没有考虑缓存的失效时间
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /**
     * @description 更新密码
     * @param userTicket
     * @param password
     * @param httpServletRequest
     * @param response
     * @return RespBean
     * @author Chenhf
     * @date 2022/7/7 21:40
     */
    @Override
    public RespBean updatePassword(String userTicket, String password,
                                   HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if (user==null){
            //如果用户不存在抛出异常
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if (result==1){
            //删除redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}


