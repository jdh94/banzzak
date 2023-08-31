package com.banzzac.auth;

import com.banzzac.auth.config.JwtConfig;
import com.banzzac.auth.domain.TokenInfo;
import com.banzzac.auth.service.AuthenticationService;
import com.banzzac.core.common.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class JwtTokenTests {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    MockHttpServletRequest request;
    @Autowired
    JwtConfig jwtConfig;
    AuthenticationService authenticationService;

    void setUp(int accessTokenExpiration, int refreshTokenExpiration, String secret){
        authenticationService = new AuthenticationService(jwtConfig);
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    public void createTokenTest(){
        //given
        setUp(60 * 60 * 12, 60 * 60 * 24 * 7, "jwtSecretKey123");
        //when
        TokenInfo tokenInfo = authenticationService.createTokenInfo("username", 1L);
        //then
        System.out.println(tokenInfo.getAccessToken());
        System.out.println(tokenInfo.getRefreshToken());
        assertNotNull(tokenInfo.getAccessToken());
        assertNotNull(tokenInfo.getRefreshToken());
    }

    @Test
    @DisplayName("어세스 토큰 유효성 검사 - 성공 케이스")
    public void accessTokenValidationSuccessful(){
        //given
        //어세스토큰 유효시간 5초, secret키 정상
        setUp(5, 10, "jwtSecretKey123");
        TokenInfo tokenInfo = authenticationService.createTokenInfo(1L);

        //when
        //4초 대기
        try {
            int sleepTime = 4000; //4000ms, 4s
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        boolean result = authenticationService.validateToken(request, tokenInfo.getAccessToken().replace(PREFIX, ""));

        //then
        assertEquals(true, result);
    }

    @Test
    @DisplayName("어세스 토큰 유효성 검사 - 유효시간 초과 실패 케이스")
    public void accessTokenValidationFailedOnTime(){
        //given
        //어세스 토큰 유효시간 1초, secret키 정상
        setUp(1, 10, "jwtSecretKey123");
        TokenInfo tokenInfo = authenticationService.createTokenInfo("username", 1L);

        //when
        //4초 대기
        try {
            int sleepTime = 4000; //4000ms, 4s
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        boolean result = authenticationService.validateToken(request, tokenInfo.getAccessToken().replace(PREFIX, ""));
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        //then
        assertEquals(false, result);
        assertEquals(ErrorCode.AUTH_EXPIRED_JWT, errorCode);
    }

    @Test
    @DisplayName("리프레시 토큰 유효성 검사 - 성공 케이스")
    public void refreshTokenValidationSuccessOnTime(){
        //given
        //어세스 토큰 유효시간 5초, secret키 정상
        setUp(1, 5, "jwtSecretKey123");
        TokenInfo tokenInfo = authenticationService.createTokenInfo("username", 1L);

        //when
        //4초 대기
        try {
            int sleepTime = 4000; //4000ms, 4s
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        boolean result = authenticationService.validateToken(request, tokenInfo.getRefreshToken());

        //then
        assertEquals(true, result);
    }

    @Test
    @DisplayName("리프레시 토큰 유효성 검사 - 실패 케이스")
    public void refreshTokenValidationFailedOnTime(){
        //given
        //어세스 토큰 유효시간 3초, secret키 정상
        setUp(1, 3, "jwtSecretKey123");
        TokenInfo tokenInfo = authenticationService.createTokenInfo("username", 1L);

        //when
        //4초 대기
        try {
            int sleepTime = 4000; //4000ms, 4s
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        boolean result = authenticationService.validateToken(request, tokenInfo.getRefreshToken());

        //then
        assertEquals(true, result);
    }
}
