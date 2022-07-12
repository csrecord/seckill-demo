package com.chenhf.vo;

import com.chenhf.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @description: 用户返回对象
 * @className: LoginVo
 * @author: Chenhf
 * @date: 2022/6/30 19:01
 * @version: 1.0
 */
//获取用户输入，与后端获取的账号密码保持一致
//相当于后端的mobile password
@Data
@SuppressWarnings("all")
public class LoginVo {
    //使用validation组件, 减少代码冗余
    @NotNull()
    //创建的自定义组件
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min=32)
    private String password;
}
