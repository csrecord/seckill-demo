package com.chenhf.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @description: MD5Util工具类
 * @className: MD5Util
 * @author: Chenhf
 * @date: 2022/6/29 19:45
 * @version: 1.0
 */
//Component注解
@Component
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /* 同步前端salt */
    private static final String salt = "1a2b3c4d";

    /**
     * @description 前端密码到后端密码加密,前端传递过来的已加密密码
     * @param inputPass
     * @return String
     * @author Chenhf
     * @date 2022/6/29 19:52
     */
    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * @description 后端到数据库加密
     * @param formPass
     * @param salt
     * @return String
     * @author Chenhf
     * @date 2022/6/29 19:56
     */
    public static String formPassToDBPass(String formPass, String salt){
        //这行代码实际没用
        String str =  "" + salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * @description 调用前端到后端加密,后端到数据库加密方法
     * @param inputPass
     * @param salt
     * @return String
     * @author Chenhf
     * @date 2022/6/29 20:00
     */
    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    /**
     * @description TODO
     * @param args
     * @return void
     * @author Chenhf
     * @date 2022/6/29 20:15
     */
    public static void main(String[] args) {
        //ce21b747de5af71ab5c2e20ff0a60eea
        System.out.println(inputPassToFormPass("123456"));
        //1897a69ef451f0991bb85c6e7c35aa31
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
