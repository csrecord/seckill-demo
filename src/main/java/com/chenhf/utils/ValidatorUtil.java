package com.chenhf.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 校验工具类
 * @className: ValidatorUtil
 * @author: Chenhf
 * @date: 2022/6/30 19:36
 * @version: 1.0
 */
public class ValidatorUtil {
    //手机号位数校验
    private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static boolean isMobile(String mobile){
        //为空直接返回
        if (StringUtils.isEmpty(mobile)){
            return false;
        }
        //根据Pattern.compile的规则校验mobile，返回boolean类型
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
