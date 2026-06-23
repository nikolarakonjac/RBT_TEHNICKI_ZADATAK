package io.github.nikolarakonjac.delivery_service_system.dto.shipment;

import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class StatusHistoryDto {
    private ShipmentState state;
    private String note;
    private Instant timeOfChange;
}
