package com.banzzac.auth.handler;

import com.banzzac.auth.service.AuthenticationService;
import com.banzzac.core.common.response.ErrorCode;
import io.jsonwebtoken.*;
import org.apache.http.util.Asserts;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
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

/**
 * Auth Server Global ExceptionHanlder
 * 인증/인가 서버에서 Exception이 발생할 경우에 대한 처리를 담당
 */
@Component
@Order(-1)
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler{

    private AuthenticationService authenticationService;

    public GlobalErrorExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
            ApplicationContext applicationContext, AuthenticationService authenticationService) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(ServerCodecConfigurer.create().getWriters());
        this.authenticationService = authenticationService;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request){
        Throwable error = getError(request);
        String requestPath = request.exchange().getRequest().getPath().value();
        ErrorCode errorCode = null;
        //토큰 유효성 검사 중 에러 발생할 경우
        if(requestPath.equals("/token/check")){
            errorCode = getTokenValidationError(error);
        }
        Asserts.notNull(errorCode, "ErrorCode");
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(CommonResponse.builder()
                        .result(Result.FAIL)
                        .data(errorCode.getErrorMsg())
                        .errorCode(errorCode.name())
                        .build()));
    }

    /**
     * 토큰 유효성 검사 중 Exception 분기 처리
     * @param error
     * @return ErrorCode
     */
    private ErrorCode getTokenValidationError(Throwable error) {
        if (error instanceof SignatureException) {
            return ErrorCode.AUTH_INVALID_SIGNATURE_JWT;
        } else if (error instanceof MalformedJwtException) {
            return ErrorCode.AUTH_INVALID_JWT;
        } else if (error instanceof ExpiredJwtException) {
            return ErrorCode.AUTH_TOKEN_EXPIRED_JWT;
        } else if (error instanceof UnsupportedJwtException) {
            return ErrorCode.AUTH_UNSUPPORTED_JWT;
        } else if (error instanceof IllegalArgumentException) {
            return ErrorCode.AUTH_ILLEGAL_ARGUMENT_JWT;
        } else{
            return ErrorCode.COMMON_SYSTEM_ERROR;
        }
    }
}
