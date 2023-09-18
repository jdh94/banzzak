package com.banzzac.core.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    COMMON_EMPTY_DATA("데이터가 존재하지 않습니다."),

    //Gateway Exception
    AUTHORIZATION_HEADER_NOT_FOUND("인증 헤더가 존재하지 않습니다."),

    // Auth Exception
    AUTH_INVALID_SIGNATURE_JWT("SIGNATURE_KEY값이 유효하지 않습니다."),
    AUTH_MALFORMED_JWT("토큰이 유효하지 않습니다."),
    AUTH_ILLEGAL_ARGUMENT_JWT("토큰이 유효하지 않습니다."),
    AUTH_UNSUPPORTED_JWT("지원하지 않는 JWT토큰입니다."),
    AUTH_INVALID_JWT("유효하지 않은 토큰입니다."),
    AUTH_EXPIRED_JWT("토큰 유효시간이 지났습니다."),
    AUTH_REFRESH_TOKEN_INVALID("리프레시 토큰이 유효하지 않습니다."),
    ;

    private final String errorMsg;

    public static ErrorCode getErrorCode(String errorCodeString){
        return ErrorCode.valueOf(errorCodeString);
    }

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
	
}
