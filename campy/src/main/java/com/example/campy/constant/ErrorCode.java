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

    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    SPRING_BAD_REQUEST(10001, HttpStatus.BAD_REQUEST, "Spring-detected bad request"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10003, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    REVIEW_NOT_FOUND(10004, HttpStatus.NOT_FOUND, "Review not found"),
    USER_NOT_FOUND(10005, HttpStatus.NOT_FOUND, "User not found"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    SPRING_INTERNAL_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Spring-detected internal error"),
    DATA_ACCESS_ERROR(20002, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error")
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
