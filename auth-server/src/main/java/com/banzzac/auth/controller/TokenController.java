package com.banzzac.auth.controller;

import com.banzzac.auth.service.AuthenticationService;
import com.banzzac.core.common.response.CommonResponse;
import com.banzzac.core.common.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 토큰 검증을 위한 컨트롤러
 */
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final AuthenticationService authenticationService;

    @PostMapping("/check")
    public ResponseEntity validateToken(HttpServletRequest request){
        boolean result = authenticationService.validateToken(request, authenticationService.resolveToken(request));
        if(!result){
            ErrorCode type = (ErrorCode) request.getAttribute("exception");
            return ResponseEntity.status(401)
                    .body(CommonResponse.builder()
                            .result(CommonResponse.Result.FAIL)
                            .errorCode(type.name())
                            .message(type.getErrorMsg())
                            .build()
                    );
        }
        return ResponseEntity.ok(CommonResponse.builder()
                .result(CommonResponse.Result.SUCCESS)
                .data(null)
                .build()
        );
    }
}
