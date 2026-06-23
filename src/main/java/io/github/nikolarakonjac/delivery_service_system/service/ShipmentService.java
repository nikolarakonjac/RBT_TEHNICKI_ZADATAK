package io.github.nikolarakonjac.delivery_service_system.service;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.StatusHistoryDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentImportResultDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.UpdateShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.entity.Shipment;
import io.github.nikolarakonjac.delivery_service_system.entity.StatusHistory;
import io.github.nikolarakonjac.delivery_service_system.entity.User;
import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import io.github.nikolarakonjac.delivery_service_system.repository.ShipmentRepository;
import io.github.nikolarakonjac.delivery_service_system.repository.UserRepository;
import io.github.nikolarakonjac.delivery_service_system.utility.exceptions.ApiExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;

    public void createShipment(NewShipmentDto newShipmentDto) {
        User user = userRepository.findById(newShipmentDto.getUserId())
                .orElseThrow(ApiExceptionFactory::userNotFound);

        Shipment newShipment = Shipment.builder()
                .description(newShipmentDto.getDescription())
                .build();

        user.addShipment(newShipment);
        shipmentRepository.save(newShipment);
    }


    public List<ShipmentDto> filterShipments(String username, ShipmentState state, LocalDate createdDate) {
        String usernameFilter = username != null && !username.isBlank() ? username.trim() : null;
        String stateFilter = state != null ? state.name() : null;

        return shipmentRepository.filterShipments(usernameFilter, stateFilter, createdDate).stream()
                .map(this::mapShipmentToDto)
                .toList();
    }

    private ShipmentDto mapShipmentToDto(Shipment shipment) {
        return ShipmentDto.builder()
                .trackerId(shipment.getTrackerId())
                .description(shipment.getDescription())
                .currentState(shipment.getCurrentState())
                .createdAt(shipment.getCreatedAt())
                .build();
    }

    public void updateShipmentState(UpdateShipmentDto updateShipmentDto) {
        Shipment shipment = shipmentRepository.findByTrackerId(updateShipmentDto.getTrackerId())
                .orElseThrow(ApiExceptionFactory::shipmentNotFound);

        shipment.setCurrentState(updateShipmentDto.getNewState());

        StatusHistory newStatusHistory = StatusHistory.builder()
                .state(updateShipmentDto.getNewState())
                .note(updateShipmentDto.getNote())
                .build();

        shipment.addStatusHistory(newStatusHistory);
        shipmentRepository.save(shipment);
    }

    public ShipmentDto getShipmentStatusHistory(UUID trackerId) {
        Shipment shipment = shipmentRepository.findByTrackerId(trackerId)
                .orElseThrow(ApiExceptionFactory::shipmentNotFound);

        return ShipmentDto.builder()
                .trackerId(shipment.getTrackerId())
                .description(shipment.getDescription())
                .currentState(shipment.getCurrentState())
                .createdAt(shipment.getCreatedAt())
                .statusHistory(mapStatusHistoryListIntoDto(shipment.getStatusHistory()))
                .build();
    }

    private List<StatusHistoryDto> mapStatusHistoryListIntoDto(List<StatusHistory> statusHistoryList){
        return statusHistoryList.stream()
                .sorted(Comparator.comparing(StatusHistory::getChangeTime))
                .map(this::mapSingleStatusHistoryIntoDto)
                .toList();
    }

    private StatusHistoryDto mapSingleStatusHistoryIntoDto(StatusHistory statusHistory){
        return StatusHistoryDto.builder()
                .state(statusHistory.getState())
                .timeOfChange(statusHistory.getChangeTime())
                .note(statusHistory.getNote())
                .build();
    }

    @Transactional
    public ShipmentImportResultDto importShipmentsFromCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw ApiExceptionFactory.invalidCsvFile("CSV file is required");
        }

        List<CsvShipmentRow> rows = parseCsvRows(file);
        List<Shipment> newShipments = new ArrayList<>();

        for (CsvShipmentRow row : rows) {
            User user = userRepository.findByUsername(row.username())
                    .orElseThrow(ApiExceptionFactory::userNotFound);

            Shipment shipment = Shipment.builder()
                    .description(row.description())
                    .currentState(row.state())
                    .build();

            user.addShipment(shipment);
            newShipments.add(shipment);
        }
        shipmentRepository.saveAll(newShipments);
        return new ShipmentImportResultDto(rows.size());
    }

    private List<CsvShipmentRow> parseCsvRows(MultipartFile file) {
        List<CsvShipmentRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                if (lineNumber == 1 && isHeaderLine(line)) {
                    continue;
                }

                rows.add(parseCsvRow(line, lineNumber));
            }
        } catch (IOException ex) {
            throw ApiExceptionFactory.invalidCsvFile("Failed to read CSV file");
        }

        if (rows.isEmpty()) {
            throw ApiExceptionFactory.invalidCsvFile("CSV file contains no shipment rows");
        }

        return rows;
    }

    private CsvShipmentRow parseCsvRow(String line, int lineNumber) {
        String[] columns = line.split(",", -1);

        if (columns.length < 1 || columns[0].trim().isEmpty()) {
            throw ApiExceptionFactory.invalidCsvFile("Missing username at line " + lineNumber);
        }

        String username = columns[0].trim();
        String description = columns.length > 1 ? emptyToNull(columns[1].trim()) : null;
        ShipmentState state = ShipmentState.CREATED;

        if (columns.length > 2 && !columns[2].trim().isEmpty()) {
            try {
                state = ShipmentState.valueOf(columns[2].trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw ApiExceptionFactory.invalidShipmentState(columns[2].trim(), lineNumber);
            }
        }

        return new CsvShipmentRow(username, description, state);
    }

    private boolean isHeaderLine(String line) {
        return line.trim().toLowerCase().startsWith("username");
    }

    private String emptyToNull(String value) {
        return value.isEmpty() ? null : value;
    }

    private record CsvShipmentRow(String username, String description, ShipmentState state) {
    }
}
