package com.leyou.auth.client;

import com.leyou.user.client.UserClientServer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient extends UserClientServer {

}
