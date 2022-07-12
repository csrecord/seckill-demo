package com.chenhf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 公共返回对象枚举
 * @author Chenhf
 * @date 2022/6/30 17:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

    private long code;
    private String message;
    private Object obj;

    /**
     * @description 成功的返回结果,只有一个
     * @param
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 17:19
     */
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    /**
     * @description 成功的返回结果重载
     * @param object
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 17:19
     */

    public static RespBean success(Object object) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), object);
    }

    /**
     * @description 失败的返回结果，失败存在很多情况
     * @param respBeanEnum
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 17:22
     */
    public static RespBean error(RespBeanEnum respBeanEnum) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    /**
     * @description TODO
     * @param respBeanEnum
     * @param obj
     * @return RespBean
     * @author Chenhf
     * @date 2022/6/30 17:25
     */
    public static RespBean error(RespBeanEnum respBeanEnum, Object obj) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), obj);
    }
}
