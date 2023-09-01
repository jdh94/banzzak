package com.banzzac.auth.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.banzzac.core.common.response.CommonResponse;
import com.banzzac.core.common.response.CommonResponse.Result;

import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler{

    public GlobalErrorExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
            ApplicationContext applicationContext) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(ServerCodecConfigurer.create().getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request){
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        request.
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(CommonResponse.builder()
        .result(Result.FAIL)
        .data(errorPropertiesMap)
        .build()));

    }

}
