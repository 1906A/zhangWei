package com.leyou.sms.listener;

import com.leyou.sms.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

public class MessageListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.send.queue", durable = "true"),
            exchange = @Exchange(value = "sms.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"sms.send"}
    ))

    public void send(Map<String, String> map) throws Exception {
        System.out.println("=====map,========phone:" + map.get("phone") + "============code:" + map.get("code"));

        if (map.size() > 0 && map == null) { //map不为空
            SmsUtils.sendMsg(map.get("phone"),map.get("code"));
        }

    }
}
