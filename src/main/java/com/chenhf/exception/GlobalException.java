package com.chenhf.exception;

import com.chenhf.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 异常处理，返回到前端，不在后端控制台输出
 * @className: GlobalException
 * @author: Chenhf
 * @date: 2022/7/1 19:25
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}
