package io.github.nikolarakonjac.delivery_service_system.service;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.dto.shipment.ShipmentDto;
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

    public ShipmentDto getSingleShipment(UUID trackerId) {
        Shipment shipment = shipmentRepository.findByTrackerId(trackerId)
                .orElseThrow(ApiExceptionFactory::shipmentNotFound);

        return ShipmentDto.builder()
                .trackerId(shipment.getTrackerId())
                .description(shipment.getDescription())
                .currentState(shipment.getCurrentState())
                .build();
    }
}
