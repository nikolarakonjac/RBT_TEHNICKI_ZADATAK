package io.github.nikolarakonjac.delivery_service_system.controller;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentImportResultDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.UpdateShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<ShipmentDto>> filterShipments(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) ShipmentState state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate
    ) {
        return ResponseEntity.ok(shipmentService.filterShipments(username, state, createdDate));
    }

    @GetMapping("/{trackerId}/status-history")
    public ResponseEntity<ShipmentDto> getShipmentStatusHistory(@PathVariable UUID trackerId){
        return ResponseEntity.ok(shipmentService.getShipmentStatusHistory(trackerId));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShipmentImportResultDto> importShipments(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.importShipmentsFromCsv(file));
    }


}
