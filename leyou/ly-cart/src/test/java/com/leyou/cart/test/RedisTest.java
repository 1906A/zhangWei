package com.leyou.cart.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {

        stringRedisTemplate.opsForHash().put("carts","id_123","{\"title\":\"手机\"}");


        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps("carts2");

        ops.put("id_234", "{\"name\":\"zhangsan\"}");
        ops.put("id_231", "{\"name\":\"zhangsan1\"}");
        ops.put("id_232", "{\"name\":\"zhangsan2\"}");
        ops.put("id_233", "{\"name\":\"zhangsan3\"}");

        Map<Object, Object> map = ops.entries();
        map.keySet().forEach(k -> {
            System.out.println(k + "----------" + map.get(k));
        });

//        System.out.println(stringRedisTemplate.boundHashOps("carts").get("id_123"));
//        System.out.println(stringRedisTemplate.opsForHash().get("carts2", "id_234"));


    }
}
