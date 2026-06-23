package io.github.nikolarakonjac.delivery_service_system.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NewUserDto {
    @NotEmpty
    private String username;
}
