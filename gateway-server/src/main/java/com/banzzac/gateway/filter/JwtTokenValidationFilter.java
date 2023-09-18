package com.banzzac.gateway.filter;

import com.banzzac.core.common.response.CommonResponse;
import com.banzzac.core.common.response.ErrorCode;
import com.banzzac.gateway.openFeign.GatewayOpenFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtTokenValidationFilter extends AbstractGatewayFilterFactory<JwtTokenValidationFilter.Config> {

    private final GatewayOpenFeignClient gatewayOpenFeignClient;
    private final ObjectMapper mapper;
    public JwtTokenValidationFilter(@Lazy GatewayOpenFeignClient gatewayOpenFeignClient, @Lazy ObjectMapper mapper){
        super(Config.class);
        this.gatewayOpenFeignClient = gatewayOpenFeignClient;
        this.mapper = mapper;
    }

    /**
     * 에러 발생 시 pre filter 에서 Error Response를 반환
     * @param response
     * @param errorCode
     * @return Json형태의 Response 객체 반환
     */
    private Mono<Void> onError(ServerHttpResponse response, ErrorCode errorCode) {
        DataBuffer buffer = null;
        try{
            byte[] bytes = mapper.writeValueAsBytes(CommonResponse.builder()
                    .result(CommonResponse.Result.FAIL)
                    .errorCode(errorCode.getErrorMsg())
                    .message(errorCode.name())
                    .build());
            buffer = response.bufferFactory().wrap(bytes);
        }catch (JsonProcessingException ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.BAD_GATEWAY);
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * AUTH-SERVICE에서 TokenValidation 실패 시 FeignException 발생
     * byte[] 값으로 되어있는 responseBody를 ObjectMapper를 사용하여 객체로 변환
     * @param throwable
     * @return Mono<CommonResponse>
     */
    private Mono<CommonResponse> createErrorObject(Throwable throwable){
        CommonResponse exceptionResult = null;
        if (throwable instanceof FeignException) {
            ByteBuffer buffer = ((FeignException) throwable).responseBody().get();
            String s = StandardCharsets.UTF_8.decode(buffer).toString();
            try {
                exceptionResult = mapper.readValue(s, CommonResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Mono.just(exceptionResult);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter : request id -> {}", request.getId());
            if(request.getHeaders().get("Authorization") == null){
                return this.onError(response, ErrorCode.AUTHORIZATION_HEADER_NOT_FOUND);
            }
            String accessToken = request.getHeaders().get("Authorization").get(0);

            return gatewayOpenFeignClient.checkTokenValidation(accessToken)
                    .onErrorResume(this::createErrorObject)
                    .flatMap(commonResponse -> {
                        if(commonResponse.getResult() == CommonResponse.Result.FAIL){
                            return this.onError(response, ErrorCode.getErrorCode(commonResponse.getErrorCode()));
                        }else{
                            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                                log.info("Custom POST filter : response code -> {}", response.getStatusCode());
                            }));
                        }
                    });
        };
    }

    public static class Config {
    }
}


