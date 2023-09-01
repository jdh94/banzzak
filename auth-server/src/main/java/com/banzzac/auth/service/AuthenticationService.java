package com.banzzac.auth.service;

import java.util.Date;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import com.banzzac.auth.domain.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;

/**
 * 토큰 관련 서비스
 */
@Service
public class AuthenticationService {
    //HttpServletRequest의 헤더에서 토큰을 추출할 때 사용
    private final static String HEADER = "Authorization";
    //AccessToken의 서두에 붙이는 String값
    private final static String TOKEN_PREFIX = "Bearer ";
    /**
     * AccessToken의 유효 시간
     * 1시간
    */ 
    private final static int ACCESS_TOKEN_EXPIRATION = 1;
    /**
     * AccessToken의 유효 시간
     * 12시간
    */ 
    private final static int REFRESH_TOKEN_EXPIRATION = 60* 60 * 12;
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
     * ServerHttpRequest Header에서 토큰 정보를 추출
     * @param request
     * @return String
     */
    public String resolveToken(ServerHttpRequest request){
        return request.getHeaders().get(HEADER).get(0).replace(TOKEN_PREFIX, "");
    }

    /**
     * 토큰의 대한 유효성 검사 메소드
     * @param request
     * @param jwtToken
     * @return 유효성 검사 성공 실패 여부
     */
    public Mono<Boolean> validateToken(ServerHttpRequest request, String jwtToken) {
        return Mono.just(!Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getExpiration()
                    .before(new Date()));
    }
}
