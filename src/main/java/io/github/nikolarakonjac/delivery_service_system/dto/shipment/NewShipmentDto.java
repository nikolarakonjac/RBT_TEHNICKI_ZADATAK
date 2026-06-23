package io.github.nikolarakonjac.delivery_service_system.dto.shipment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class NewShipmentDto {

    @NonNull
    private Long userId;

    @NotBlank
    private String description;
}
