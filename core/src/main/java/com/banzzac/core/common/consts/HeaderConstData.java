package com.banzzac.core.common.consts;

public class HeaderConstData {
    //HttpServletRequest의 헤더에서 토큰을 추출할 때 사용
    public final static String AUTHORIZATION_HEADER = "Authorization";
    //HttpServletRequest의 헤더에서 토큰의 타입을 추출
    public final static String TOKEN_TYPE_HEADER = "Token-Type";
    //AccessToken의 서두에 붙이는 String값
    public final static String TOKEN_PREFIX = "Bearer ";
}
