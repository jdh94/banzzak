package com.banzzac.gateway.openFeign;

import com.banzzac.core.common.response.CommonResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "AUTH-SERVER")
public interface GatewayOpenFeignClient {

    @PostMapping("/token/check")
    Mono<CommonResponse> checkTokenValidation(@RequestHeader("Authorization") String authorization, @RequestHeader("Token-Type") String tokenType);
}
