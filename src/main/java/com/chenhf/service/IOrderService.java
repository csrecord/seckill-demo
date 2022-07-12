package com.chenhf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenhf.pojo.Order;
import com.chenhf.pojo.User;
import com.chenhf.vo.GoodsVo;
import com.chenhf.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenhf
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);

    Boolean checkPath(User user, String path, Long goodsId);

    Boolean checkCptcha(User user, Long goodsId, String captcha);
}
