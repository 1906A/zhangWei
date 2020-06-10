package com.leyou.search.listener;

import com.leyou.search.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {
    @Autowired
    GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.edit.search.queue", durable = "true"),
            exchange = @Exchange(value = "item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}
    ))

    public void editEs(Long spuId) throws Exception {
        System.out.println("=====editEs======1:"+spuId);
        if (spuId==null){
            return;
        }
        //根据spuId查询spu
        goodsService.editEs(spuId);
        System.out.println("------editEs-----2:"+spuId);

    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "item.delete.search.queue", durable = "true"),
            exchange = @Exchange(value = "item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))

    public void deleteEs(Long spuId) throws Exception {
        System.out.println("=====deleteEs======1:"+spuId);
        if (spuId==null){
            return;
        }
        //根据spuId查询spu
        goodsService.deleteEs(spuId);
        System.out.println("------deleteEs-----2:"+spuId);

    }
}
