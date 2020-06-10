package com.leyou.lygoods.listener;

import com.leyou.lygoods.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.edit.web.queue", durable = "true"),
            exchange = @Exchange(value = "item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))

    public void editThymeleaf(Long spuId) throws Exception {
        System.out.println("====editThymeleaf=======1:" + spuId);

        //根据spuId查询spu
        if (spuId == null) {
            return;
        }
        goodsService.createHtml(spuId);
        System.out.println("----editThymeleaf-------2:" + spuId);

    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.delete.web.queue", durable = "true"),
            exchange = @Exchange(value = "item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))

    public void deleteThymeleaf(Long spuId) throws Exception {
        System.out.println("====deleteThymeleaf=======1:" + spuId);

        //根据spuId查询spu
        if (spuId == null) {
            return;
        }
        goodsService.deleteHtml(spuId);
        System.out.println("----deleteThymeleaf-------2:" + spuId);

    }
}
