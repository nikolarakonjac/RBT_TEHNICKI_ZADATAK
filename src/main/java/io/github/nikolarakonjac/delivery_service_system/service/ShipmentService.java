package io.github.nikolarakonjac.delivery_service_system.service;

import io.github.nikolarakonjac.delivery_service_system.dto.shipment.NewShipmentDto;
import io.github.nikolarakonjac.delivery_service_system.entity.Shipment;
import io.github.nikolarakonjac.delivery_service_system.entity.User;
import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import io.github.nikolarakonjac.delivery_service_system.repository.ShipmentRepository;
import io.github.nikolarakonjac.delivery_service_system.repository.UserRepository;
import io.github.nikolarakonjac.delivery_service_system.utility.exceptions.ApiExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
