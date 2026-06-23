package io.github.nikolarakonjac.delivery_service_system.dto.shipment;

import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ShipmentDto {
    private UUID trackerId;
    private String description;
    private ShipmentState currentState;
}
