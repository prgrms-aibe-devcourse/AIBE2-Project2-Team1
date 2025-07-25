package com.example.campy.constant;

import com.example.campy.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(0, HttpStatus.OK, "성공"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "잘못된 요청"),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "스프링 감지 잘못된 요청"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "유효성 검사 오류"),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없음"),
    REVIEW_NOT_FOUND(10004, HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없음"),
    USER_NOT_FOUND(10005, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없음"),
    DUPLICATE_EMAIL(10006, HttpStatus.CONFLICT, "중복된 이메일"),
    UNAUTHORIZED(10007, HttpStatus.FORBIDDEN, "권한 없음"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류"),
    SPRING_INTERNAL_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "스프링 감지 내부 서버 오류"),
    DATA_ACCESS_ERROR(20002, HttpStatus.INTERNAL_SERVER_ERROR, "데이터 접근 오류")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) { throw new GeneralException("HttpStatus is null."); }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus().equals(httpStatus))
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) { return ErrorCode.BAD_REQUEST; }
                    else if (httpStatus.is5xxServerError()) { return ErrorCode.INTERNAL_ERROR; }
                    else { return ErrorCode.OK; }
                });
    }

    public String getDetailedMessage(Throwable e) {
        return String.format("%s - %s", this.message, e.getMessage());
    }

    public String withMessage(String customMessage) {
        return Optional.ofNullable(customMessage)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.message);
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

}