package io.github.nikolarakonjac.delivery_service_system.utility.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@ComponentScan
@Component
@Configuration
public class ExceptionHandlerAdvice {

    /**
     * Handles Api exceptions
     *
     * @param ex ApiException
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessageDto> handleApiException(ApiException ex) {

        log.error("Api exception: ", ex);
        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessageDto(ex));

    }
}
