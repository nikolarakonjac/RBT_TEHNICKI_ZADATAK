package io.github.nikolarakonjac.delivery_service_system.controller;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentImportResultDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.UpdateShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import io.github.nikolarakonjac.delivery_service_system.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "Shipment management, filtering, status updates and CSV import")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Operation(summary = "Create shipment", description = "Creates a new shipment for an existing user. Initial state is CREATED.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shipment created"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<Void> createShipment(@RequestBody @Valid NewShipmentDto newShipment) {
        shipmentService.createShipment(newShipment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update shipment state", description = "Updates shipment current state and appends a new status history entry.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "State updated"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @PutMapping
    public ResponseEntity<Void> updateShipmentState(@RequestBody @Valid UpdateShipmentDto updateShipmentDto) {
        shipmentService.updateShipmentState(updateShipmentDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Filter shipments",
            description = "Returns shipments matching optional filters. All parameters are optional and combined with AND logic."
    )
    @ApiResponse(responseCode = "200", description = "Filtered list of shipments")
    @GetMapping
    public ResponseEntity<List<ShipmentDto>> filterShipments(
            @Parameter(description = "Filter by owner's username")
            @RequestParam(required = false) String username,
            @Parameter(description = "Filter by current shipment state")
            @RequestParam(required = false) ShipmentState state,
            @Parameter(description = "Filter by creation date in UTC (ISO format: yyyy-MM-dd)", example = "2026-06-23")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate
    ) {
        return ResponseEntity.ok(shipmentService.filterShipments(username, state, createdDate));
    }

    @Operation(summary = "Get shipment status history", description = "Returns shipment details with status history sorted ascending by change time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shipment with status history"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @GetMapping("/{trackerId}/status-history")
    public ResponseEntity<ShipmentDto> getShipmentStatusHistory(
            @Parameter(description = "Shipment tracker UUID")
            @PathVariable UUID trackerId
    ) {
        return ResponseEntity.ok(shipmentService.getShipmentStatusHistory(trackerId));
    }

    @Operation(
            summary = "Import shipments from CSV",
            description = "CSV columns: username (required), description (optional), state (optional, defaults to CREATED)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shipments imported"),
            @ApiResponse(responseCode = "400", description = "Invalid CSV file"),
            @ApiResponse(responseCode = "404", description = "User from CSV not found")
    })
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShipmentImportResultDto> importShipments(
            @Parameter(
                    description = "CSV file with columns: username, description, state",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.importShipmentsFromCsv(file));
    }

}
