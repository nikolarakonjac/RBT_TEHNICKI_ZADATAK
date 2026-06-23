package io.github.nikolarakonjac.delivery_service_system.controller;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.UpdateShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<Void> createShipment(@RequestBody @Valid NewShipmentDto newShipment) {
        shipmentService.createShipment(newShipment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateShipmentState(@RequestBody @Valid UpdateShipmentDto updateShipmentDto){
        shipmentService.updateShipmentState(updateShipmentDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{trackerId}")
    public ResponseEntity<ShipmentDto> getShipment(@PathVariable UUID trackerId){
        return ResponseEntity.ok(shipmentService.getSingleShipment(trackerId));
    }


}
