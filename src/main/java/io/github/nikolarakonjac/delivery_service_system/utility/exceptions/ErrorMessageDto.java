package io.github.nikolarakonjac.delivery_service_system.utility.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nikolarakonjac.delivery_service_system.utility.http.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessageDto {

    private ErrorCode code;

    @JsonInclude
    private String message;

    private String content;

    private Object originalErrorMessage;

    public ErrorMessageDto(ApiException ex) {
        this.code = ex.getErrorCode();
        this.message = ex.getMessage();
        this.originalErrorMessage = ex.getErrorMessage();
        if (ex.getErrorMessage() == null) this.content = Arrays.toString(ex.getStackTrace());
    }
}
