package com.banzzac.auth.service;

import java.util.Date;
import static com.banzzac.core.common.consts.HeaderConstData.*;

import com.banzzac.auth.enums.TokenType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import com.banzzac.auth.domain.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

/**
 * 토큰 관련 서비스
 */
@Service
public class AuthenticationService {

    /**
     * AccessToken의 유효 시간
     * 1시간
     */
    private final static int ACCESS_TOKEN_EXPIRATION = 60 * 60;

    /**
     * AccessToken의 유효 시간
     * 12시간
     */
    private final static int REFRESH_TOKEN_EXPIRATION = 60 * 60 * 12;
    //토큰 암호화 키
    private final static String SECRET = "my_symmetri1c_key";

    /**
     * 토큰 생성 메소드
     * AccessToken, RefreshToken 생성
     * 클레임은 일단 id 설정
     * @param userNo
     * @return TokenInfo
     */
    public TokenInfo createTokenInfo(Long userNo){
        Claims claims = Jwts.claims();
        claims.put("id", userNo);
        return TokenInfo.builder()
                .accessToken(TOKEN_PREFIX + generateTokenString(claims, new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000L)))
                .refreshToken(generateTokenString(claims, new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION * 1000L)))
                .build();
    }

    /**
     * 토큰 재발급을 위한 메소드
     * RefreshToken은 기존의 토큰을 그대로 사용
     * @param refreshToken
     * @return
     */
    public Mono<TokenInfo> createNewAccessToken(String refreshToken) {
        Claims claims = Jwts.claims();
        claims.put("id", Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(refreshToken)
                .getBody()
                .get("id").toString());
        return Mono.just(TokenInfo.builder()
                .accessToken(TOKEN_PREFIX + generateTokenString(claims, new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION * 1000L)))
                .refreshToken(refreshToken)
                .build());
    }

    /**
     * 클레임, 발행 시간, 유효 시간, 암호화 알고리즘 방식 설정
     * @param claims
     * @param expirationDt
     * @return AccessToken 혹은 RefreshToken의 String값
     */
    public String generateTokenString(Claims claims, Date expirationDt) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDt)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 토큰 추출
     * @param request
     * @return
     */
    public String resolveToken(ServerHttpRequest request){
        HttpHeaders headers = request.getHeaders();
        if(headers.get(TOKEN_TYPE_HEADER).get(0).equals(TokenType.ACCESS.name())){
            return headers.get(AUTHORIZATION_HEADER).get(0).replace(TOKEN_PREFIX, "");
        }else {
            return headers.get(AUTHORIZATION_HEADER).get(0);
        }
    }

    /**
     * 토큰의 대한 유효성 검사 메소드
     * @param jwtToken
     * @return 유효성 검사 성공 실패 여부
     */
    public Mono<Boolean> validateToken(String jwtToken) {
        return Mono.just(!Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getExpiration()
                    .before(new Date()));
    }
}
