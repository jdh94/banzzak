package com.banzzac.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtConfig {
    //HttpServletRequest의 헤더에서 토큰을 추출할 때 사용
    @Value("${security.jwt.header:Authorization}")
    private String header;

    //AccessToken의 서두에 붙이는 String값
    @Value("${security.jwt.prefix:Bearer }")
    private String tokenPrefix;

    //AccessToken의 유효 시간
    @Value("${security.jwt.expiration:#{60 * 60 * 12}}")
    private int accessTokenExpiration;

    //RefreshToken의 유효 시간
    @Value("${security.jwt.expiration:#{60 * 60 * 24 * 7}}")
    private int refreshTokenExpiration;

    //토큰 암호화 키
    @Value("${security.jwt.secret:jwtSecretKey123}")
    private String secret;
}
