package io.github.nikolarakonjac.delivery_service_system.utility.exceptions;

import io.github.nikolarakonjac.delivery_service_system.utility.http.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;
    private final String errorMessage;

    public ApiException(Throwable cause, HttpStatus httpStatus, ErrorCode errorCode, String errorMessage) {
        super(errorMessage, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String errorMessage) {
        this(null, httpStatus, errorCode, errorMessage);
    }
}
