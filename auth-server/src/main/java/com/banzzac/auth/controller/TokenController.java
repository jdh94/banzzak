package com.banzzac.auth.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import com.banzzac.auth.domain.TokenInfo;
import com.banzzac.auth.service.AuthenticationService;
import com.banzzac.core.common.response.CommonResponse;
import com.banzzac.core.common.response.CommonResponse.Result;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


/**
 * 토큰 검증을 위한 컨트롤러
 */
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final AuthenticationService authenticationService;

    @PostMapping("/check")
    public Mono<CommonResponse> validateToken(ServerHttpRequest request){
        return authenticationService.validateToken(authenticationService.resolveToken(request))
        .then(Mono.just(CommonResponse.builder()
            .result(Result.SUCCESS)
            .data(null)
            .build()));
    }

    /**
     * 토큰 재발급 API
     * @return
     */
    @GetMapping("/refresh")
    Mono<CommonResponse> publishByRefreshToken(ServerHttpRequest request){
        return authenticationService.createNewAccessToken(authenticationService.resolveToken(request))
                .map(tokenInfo -> CommonResponse.builder()
                        .result(Result.SUCCESS)
                        .data(tokenInfo)
                        .build());
    }

    /**
     * 임의 토큰 발급 API
     * @param request
     * @return
     */
    @GetMapping
    Mono<TokenInfo> test(ServerHttpRequest request){
        return Mono.just(authenticationService.createTokenInfo(1L));
    }
}
