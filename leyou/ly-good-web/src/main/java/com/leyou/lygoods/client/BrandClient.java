package com.leyou.lygoods.client;

import com.leyou.client.BrandClientServer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface BrandClient extends BrandClientServer {
}
