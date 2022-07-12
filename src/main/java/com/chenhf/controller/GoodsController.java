package com.chenhf.controller;

import com.chenhf.pojo.User;
import com.chenhf.service.IGoodsService;
import com.chenhf.service.IUserService;
import com.chenhf.service.serviceImpl.UserServiceImpl;
import com.chenhf.vo.DetailVo;
import com.chenhf.vo.GoodsVo;
import com.chenhf.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @description: 登录成功跳转商品页面
 * @className: GoodsController
 * @author: Chenhf
 * @date: 2022/7/1 20:07
 * @version: 1.0
 */
@Controller
@RequestMapping("/goods")
@SuppressWarnings("all")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    //引入redis优化页面
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

   /**
    * @description 接口测试：windows QPS（50000次）优化前4012.2  优化后
    *                       linux   QPS(10000次) 优化前395.6    优化后
    * @param model
    * @param user
    * @return String
    * @author Chenhf
    * @date 2022/7/4 16:37
    */
    //redis缓存优化商品处理页
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,HttpServletRequest request,HttpServletResponse response) {
        //通过redis valueOperations获取页面
        //redis中获取页面,如果不为空直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        //return "goodsList";
        //如果页面为null,手动渲染存入redis返回
        WebContext context = new WebContext(request, response,request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    //相当于返回一个对象
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId){

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        //定义秒杀状态
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        if (nowDate.before(startDate)){
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        //做了一个公共的返回对象,之前通过model传参,后面通过DetailVo传参
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
