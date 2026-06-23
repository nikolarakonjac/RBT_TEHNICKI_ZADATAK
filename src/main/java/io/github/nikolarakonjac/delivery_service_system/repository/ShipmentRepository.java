package io.github.nikolarakonjac.delivery_service_system.repository;

import io.github.nikolarakonjac.delivery_service_system.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Boolean existsByTrackerId(UUID trackerId);

    Optional<Shipment> findByTrackerId(UUID trackerId);
}
