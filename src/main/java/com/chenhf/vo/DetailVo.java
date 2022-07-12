package com.chenhf.vo;

import com.chenhf.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @className: DetailVo
 * @author: Chenhf
 * @date: 2022/7/7 22:09
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {
    private User user;

    private GoodsVo goodsVo;

    private int seckillStatus;

    private int remainSeconds;
}
