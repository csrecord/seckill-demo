package com.chenhf.config;

import com.chenhf.pojo.User;
import com.chenhf.service.IUserService;
import com.chenhf.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 判断ticket是否为null，传到对应的controller中去
 * @className: UserArgumentResolvers
 * @author: Chenhf
 * @date: 2022/7/4 16:25
 * @version: 1.0
 */
@Component
public class UserArgumentResolvers implements HandlerMethodArgumentResolver {
    @Autowired
    private IUserService userService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        //cookie
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        //查看用是否存在
        if (StringUtils.isEmpty(ticket)){
            return null;
        }
        //获取user的方法,根据ticket获取对应的user
        return userService.getUserByCookie(ticket, request, response);
    }
}
