package com.chenhf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenhf.pojo.SeckillOrder;
import com.chenhf.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenhf
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * @description 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId:成功,-1:秒杀失败,0:排队中
     * @author Chenhf
     * @date 2022/7/10 16:07
     */
    Long getResult(User user, Long goodsId);
}
