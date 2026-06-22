package io.github.nikolarakonjac.delivery_service_system.repository;

import io.github.nikolarakonjac.delivery_service_system.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
