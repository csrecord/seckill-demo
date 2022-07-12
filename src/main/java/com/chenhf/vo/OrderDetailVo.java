package com.chenhf.vo;

import com.chenhf.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @className: OrderDetailVo
 * @author: Chenhf
 * @date: 2022/7/8 0:10
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private Order order;
    private GoodsVo goodsVo;

}
