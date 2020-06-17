package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.utils.CodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 校验
     * 用户,手机号
     *
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public Boolean check(@PathVariable("data") String data, @PathVariable Integer type) {
        System.out.println("check:======" + data + "======" + type);
        return userService.check(data, type);
    }

    /**
     * 根据手机号生成随机6位数验证码
     *
     * @param phone
     */
    @PostMapping("code")
    public void code(@RequestParam("phone") String phone) {
        System.out.println("code:=======" + phone);

        //1.生成随机码
        String code = CodeUtils.messageCode(6);

        //2.使用rabbitmq发送短信服务给指定手机号发送验证码
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        //参数1.交换机名字  2.routing key  3.内容
//        amqpTemplate.convertAndSend("sms.exchange","sms.send",map);

        //3.验证码存放到redis
        //参数 1.key  2.value  3.long类型的时间值  4.单位,分钟  五分钟
        stringRedisTemplate.opsForValue().set("lysms_" + phone, code, 50, TimeUnit.DAYS);
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     */
    @PostMapping("register")
    public void register(@Valid User user, String code) {
        System.out.println("register:======" + user + "========" + code);


        if (user != null) {

            //1.判断code是否一致
            String s = stringRedisTemplate.opsForValue().get("lysms_" + user.getPhone());
            if (s.equals(code)) {
                userService.insertUser(user);
            }
        }

    }

    /**
     * 根据用户名密码查询用户对象
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User query(@RequestParam("username") String username, @RequestParam("password") String password) {
        //1：根据用户名查询用户信息
        User user  = userService.findUser(username);
        if(user!=null){
            //2：比对密码
            String newPassword = DigestUtils.md5Hex(password + user.getSalt());
            System.out.println("newPassword:"+newPassword);
            System.out.println("password:"+user.getPassword());
            if(user.getPassword().equals(newPassword)){
                return user;
            }
        }
        return null;
    }

    /**
     * 登录
     * 根据用户名密码查询用户对象
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public Integer login(@RequestParam("username") String username, @RequestParam("password") String password) {
        //根据用户名返回用户对象
        User user = userService.findUser(username);
        if (user != null) {
            //对比密码
            String pwd = DigestUtils.md5Hex(password + user.getSalt());
            if (pwd.equals(user.getPassword())) {
                return 1;
            }
        }

        return 0;
    }

}
