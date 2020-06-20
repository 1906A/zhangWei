package com.leyou.cart.controller;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.common.UserInfo;
import com.leyou.cart.config.JwtProperties;
import com.leyou.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    /**
     * 购物车数据存放redis
     * <p>
     * 操作需要验证是否是登录状态,使用 jwt校验 token信息
     */

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    JwtProperties jwtProperties;

    public String prifix = "ly_carts";


    /**
     * 根据 token解析用户
     *
     * @param token
     * @return
     */
    public UserInfo getUserInfoByToken(String token) {

        UserInfo userInfo = null;
        try {

            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    /**
     * @param sku
     * @param token 添加购物车数据到redis,@CookieValue("token") 从浏览器的cookie中取值叫token的信息
     */
    @RequestMapping("addCart")
    public void addCart(@CookieValue("token") String token, @RequestBody Sku sku) {
        //校验用户状态
        UserInfo userInfo = this.getUserInfoByToken(token);

        if (userInfo.getId() != null) {

            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());

            if (ops.hasKey(sku.getId())){

            }else {
                ops.put(sku.getId(), sku);
            }

        }


    }

    @RequestMapping("updateCart")
    public void updateCart(@RequestBody Sku sku) {

    }

    @RequestMapping("deleteCart")
    public void deleteCart(@RequestBody Long skuId) {

    }

    @RequestMapping("getCart")
    public void getCart(@RequestBody Sku sku) {

    }

}
