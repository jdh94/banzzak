package com.banzzac.auth.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Mono<CommonResponse<?>> validateToken(ServerHttpRequest request){
        return authenticationService.validateToken(request, authenticationService.resolveToken(request))
        .then(Mono.just(CommonResponse.builder()
            .result(Result.SUCCESS)
            .data(null)
            .build()));
    }

    @GetMapping
    Mono<TokenInfo> test(ServerHttpRequest request){
        return Mono.just(authenticationService.createTokenInfo(1L));
    }
}
