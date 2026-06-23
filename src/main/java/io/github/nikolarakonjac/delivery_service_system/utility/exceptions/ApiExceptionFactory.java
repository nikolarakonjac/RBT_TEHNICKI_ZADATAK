package io.github.nikolarakonjac.delivery_service_system.utility.exceptions;


import io.github.nikolarakonjac.delivery_service_system.utility.http.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiExceptionFactory {

    public static ApiException userNotFound() {
        return new ApiException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "User doesn't exist");
    }

    public static ApiException userAlreadyExists() {
        return new ApiException(HttpStatus.CONFLICT, ErrorCode.CONFLICT, "User already exist");
    }

    public static ApiException shipmentNotFound() {
        return new ApiException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "Shipment doesn't exist");
    }

}
