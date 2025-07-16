package com.example.campy.exception;

import com.example.campy.constant.ErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    public GeneralException() {
        super(ErrorCode.INTERNAL_ERROR.getMessage());
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(String message) {
        super(ErrorCode.INTERNAL_ERROR.withMessage(message));
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR.withMessage(message), cause);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR.getDetailedMessage(cause), cause);
        this.errorCode = ErrorCode.INTERNAL_ERROR;
    }

    public GeneralException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GeneralException(ErrorCode errorCode, String message) {
        super(errorCode.withMessage(message));
        this.errorCode = errorCode;
    }

    public GeneralException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode.withMessage(message), cause);
        this.errorCode = errorCode;
    }

    public GeneralException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDetailedMessage(cause), cause);
        this.errorCode = errorCode;
    }
}
