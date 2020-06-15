package com.leyou.user.service;

import com.leyou.user.dao.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    /**
     * 对用户名,手机号,校验
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean check(String data, Integer type) {
        Boolean result=false;
        User user = new User();

        //type=1用户名校验,type=2手机号校验
        if (type==1){   //用户名是否存在

            user.setUsername(data);

        }else if (type==2){     //手机号是否存在
            user.setPhone(data);
        }

        User selectOne = userMapper.selectOne(user);
        if (selectOne==null){
            return true;
        }
        return result;
    }

    /**
     * 将注册信息添加到user表
     *
     * @param user
     */
    public void insertUser(User user) {

        //随机生成字符串
        String salt = UUID.randomUUID().toString().substring(0,32);

        String pwd = this.getPwd(user.getPassword(), salt);

        user.setPassword(pwd);

        user.setCreated(new Date());

        user.setSalt(salt);

        userMapper.insert(user);
    }

    /**
     * 通过原生密码+盐值生成md5加密密码
     * 返回加密后的密文
     *
     * @param password
     * @param salt
     * @return
     */
    public String getPwd(String password,String salt){

        //md5加密
        String md5Hex = DigestUtils.md5Hex(password + salt);

        return md5Hex;

    }

    public User findUser(String username) {
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        return user1;
    }
}
