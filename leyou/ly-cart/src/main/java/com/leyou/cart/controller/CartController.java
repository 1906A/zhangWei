package com.leyou.cart.controller;

import com.leyou.auth.common.JwtUtils;
import com.leyou.auth.common.UserInfo;
import com.leyou.cart.config.JwtProperties;
import com.leyou.cart.pojo.SkuVo;
import com.leyou.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String prifix = "ly_carts_";
    public String prifixSelectedSku = "ly_carts_SelectedSku_";


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
     * @param skuVo
     * @param token 添加购物车数据到redis,@CookieValue("token") 从浏览器的cookie中取值叫token的信息
     */
    @RequestMapping("addCart")
    public void addCart(@CookieValue("token") String token, @RequestBody SkuVo skuVo) {
        //校验用户状态
        UserInfo userInfo = this.getUserInfoByToken(token);

        //登陆状态下
        if (userInfo.getId() != null) {

            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());


            if (ops.hasKey(skuVo.getId().toString())) {//当key中已经有id存在,非第一次添加
                //从redis中根据指定key获取value , 是json类型
                Object sku = ops.get(skuVo.getId().toString());
                //使用工具类解析json格式
                SkuVo redisSkuVo = JsonUtils.parse(sku.toString(), SkuVo.class);

                //修改购物车sku新的数量
                redisSkuVo.setNum(redisSkuVo.getNum() + skuVo.getNum());

                //修改redis,存redis的value是json格式
                ops.put(skuVo.getId().toString(), JsonUtils.serialize(redisSkuVo));


                //存当前操作的商品
                stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).set(JsonUtils.serialize(skuVo));

            } else {     //第一次添加,存redis的value是json格式
                ops.put(skuVo.getId().toString(), JsonUtils.serialize(skuVo));

                //存当前操作的商品
                stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).set(JsonUtils.serialize(skuVo));
            }

        }
    }

    /**
     * redis获取当前操作的sku
     *
     * @param token
     * @return
     */
    @RequestMapping("selectedSkuVo")
    public SkuVo skuVo(@CookieValue("token") String token) {
        //获取当前用户信息
        UserInfo userInfo = this.getUserInfoByToken(token);

        //指定key获取当前操作的sku数据
        String key = stringRedisTemplate.boundValueOps(prifixSelectedSku + userInfo.getId()).get();
//解析value的json格式,返回当前的操作的商品数据
        SkuVo skuVo = JsonUtils.parse(key, SkuVo.class);

        return skuVo;
    }

    /**
     * 修改购物车
     *
     * @param token
     * @param skuVo
     */
    @RequestMapping("updateCart")
    public void updateCart(@CookieValue("token") String token, @RequestBody SkuVo skuVo) {
        //校验用户状态
        UserInfo userInfo = this.getUserInfoByToken(token);

        //登陆状态下
        if (userInfo.getId() != null) {

            //往redis中存入hash类型,大key是指定前缀的常量prifix,key是每个sku的id,value是sku所有数据
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());


            if (ops.hasKey(skuVo.getId().toString())) {//当key中已经有id存在,非第一次添加
                //从redis中根据指定key获取value , 是json类型
                Object sku = ops.get(skuVo.getId().toString());
                //使用工具类解析json格式
                SkuVo redisSkuVo = JsonUtils.parse(sku.toString(), SkuVo.class);

                //修改购物车的数量是每次传过来的数量,不用累加
                redisSkuVo.setNum(skuVo.getNum());

                //修改redis,存redis的value是json格式
                ops.put(skuVo.getId().toString(), JsonUtils.serialize(redisSkuVo));

            } else {     //第一次添加,存redis的value是json格式
                ops.put(skuVo.getId().toString(), JsonUtils.serialize(skuVo));
            }

        }

    }

    /**
     * 删除购物车
     *
     * @param token
     * @param skuId
     */
    @RequestMapping("deleteCart")
    public void deleteCart(@CookieValue("token") String token, @RequestParam("id") Long skuId) {
        //校验用户状态
        UserInfo userInfo = this.getUserInfoByToken(token);

        //登陆状态下
        if (userInfo.getId() != null) {

            //往redis中存入hash类型,大key是指定前缀的常量prifix,key是每个sku的id,value是sku所有数据
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());

            //根据key为skuID,删除
            ops.delete(skuId.toString());


        }

    }

    /**
     * 获取购物车信息
     *
     * @param token
     * @return
     */
    @RequestMapping("getCart")
    public List<SkuVo> getCart(@CookieValue("token") String token) {
//校验用户状态
        UserInfo userInfo = this.getUserInfoByToken(token);
        List<SkuVo> list = new ArrayList();

        //登陆状态下
        if (userInfo.getId() != null) {

            //往redis中存入hash类型,大key是指定前缀的常量prifix,key是每个sku的id,value是sku所有数据
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(prifix + userInfo.getId());


            //遍历
            Map<Object, Object> map = ops.entries();
            //拿到所有key是集合,遍历
            map.keySet().forEach(key -> {
                //拿到的value转json格式为skuVo,存入list集合
                list.add(JsonUtils.parse(ops.get(key).toString(), SkuVo.class));
            });

        }
        return list;
    }

}
