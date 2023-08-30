package com.banzzac.auth.service;

import com.banzzac.auth.config.JwtConfig;
import com.banzzac.auth.domain.TokenInfo;
import com.banzzac.core.common.response.ErrorCode;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 토큰 관련 서비스
 */
@Service
public class AuthenticationService {
    private final JwtConfig jwtConfig;

    public AuthenticationService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

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
                .accessToken(jwtConfig.getTokenPrefix() + generateTokenString(claims, new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration() * 1000L)))
                .refreshToken(generateTokenString(claims, new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration() * 1000L)))
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
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    /**
     * HttpServletRequest의 Header에서 토큰 정보를 추출
     * @param request
     * @return String
     */
    public String resolveToken(HttpServletRequest request){
        return request.getHeader(jwtConfig.getHeader()).replace(jwtConfig.getTokenPrefix(), "");
    }

    /**
     * 토큰의 대한 유효성 검사 메소드
     * @param request
     * @param jwtToken
     * @return 유효성 검사 성공 실패 여부
     */
    public boolean validateToken(HttpServletRequest request, String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException ex) {
            request.setAttribute("exception", ErrorCode.AUTH_INVALID_SIGNATURE_JWT);
        } catch (MalformedJwtException ex) {
            request.setAttribute("exception", ErrorCode.AUTH_INVALID_JWT);
        } catch (ExpiredJwtException ex) {
            request.setAttribute("exception", ErrorCode.AUTH_EXPIRED_JWT);
        } catch (UnsupportedJwtException ex) {
            request.setAttribute("exception", ErrorCode.AUTH_UNSUPPORTED_JWT);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("exception", ErrorCode.AUTH_ILLEGAL_ARGUMENT_JWT);
        }
        return false;
    }
}
