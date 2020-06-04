package com.leyou.lygoods.client;

import com.leyou.client.SpecGroupClientServer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecGroupClient extends SpecGroupClientServer {

}
