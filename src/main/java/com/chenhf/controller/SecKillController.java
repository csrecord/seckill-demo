package com.chenhf.controller;

import com.alibaba.fastjson.JSON;
import com.chenhf.exception.GlobalException;
import com.chenhf.pojo.SeckillMessage;
import com.chenhf.pojo.SeckillOrder;
import com.chenhf.pojo.User;
import com.chenhf.rabbitmq.MQSender;
import com.chenhf.service.IGoodsService;
import com.chenhf.service.IOrderService;
import com.chenhf.service.ISeckillOrderService;
import com.chenhf.vo.GoodsVo;
import com.chenhf.vo.RespBean;
import com.chenhf.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 秒杀Controller
 * @className: SecKillController
 * @author: Chenhf
 * @date: 2022/7/6 14:49
 * @version: 1.0
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
//实现InitializingBean初始化时的一些方法:将商品库存加载到redis中
public class SecKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> script;

    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();

    //跳转秒杀页面
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //1.实现after引入库存,通过redis操作
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean check = orderService.checkPath(user, path, goodsId);
        if(!check) {
            return RespBean.error(RespBeanEnum.PATH_ERROR);
        }

        //判断是否重复抢购, t_seckill_order添加唯一索引user_id和goods_id
        //2.存入redis在这里获取秒杀订单,在系统初始化时就把商品库存加载到redis中,通过redis预减库存,减少数据库操作
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //通过内存标记减少redis访问
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //3.decrement是原子性的,用它来预减库存操作
        //Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //分布式锁预减库存
        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock == 0) {
            EmptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:"+goodsId);//由于递减之后是一个负数,这里递加为零
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //4.下单流程,将用户信息及下单商品ID放入RabbitMQ中
        //用户信息及下单商品ID对象
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        //对象转为字符串
        mqSender.sendSeckillMessage(JSON.toJSONString(seckillMessage));
        //秒杀订单进入排队状态
        return RespBean.success(0);
    }

    /**
     * @description 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId:成功,-1:秒杀失败,0:排队中
     * @author Chenhf
     * @date 2022/7/10 16:04
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId){
        if (user == null){
            return RespBean.error(RespBeanEnum.ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    //获取秒杀的接口
    @RequestMapping("/path")
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        //判断验证码是否正确
        Boolean check = orderService.checkCptcha(user, goodsId, captcha);
        if(!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }

        //生成秒杀的接口
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    //生成验证码
    @RequestMapping("/captcha")
    public void vertifyCode(User user, Long goodsId, HttpServletResponse response) {
        if(user == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //设置请求头为输出图片数据
        response.setContentType("image/jpg");
        //不需要缓存，每次都能获取新的验证码
        response.setHeader("Pargam","No-cache");
        response.setHeader("Cache-Control", "no-cache");
        //失效时间
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入redis中
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

    /**
     * @description 初始化时将商品库存加载到redis中,通过redis扣减库存,通过rabbit下单操作
     * @param
     * @return void
     * @author Chenhf
     * @date 2022/7/10 20:28
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)){
            return ;
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        for (GoodsVo goodsVo : list){
            valueOperations.set("seckillGoods:"+goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        }
    }
}
