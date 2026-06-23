package io.github.nikolarakonjac.delivery_service_system.dto.shipment;

import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateShipmentDto {
    @NotNull
    private UUID trackerId;
    @NotNull
    private ShipmentState newState;
    private String note;
}
