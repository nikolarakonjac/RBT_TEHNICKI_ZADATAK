package io.github.nikolarakonjac.delivery_service_system.repository;

import io.github.nikolarakonjac.delivery_service_system.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Boolean existsByTrackerId(UUID trackerId);

    Optional<Shipment> findByTrackerId(UUID trackerId);

    @Query(value = """
            SELECT s.*
            FROM shipments s
            JOIN users u ON u.id = s.user_id
            WHERE (CAST(:username AS text) IS NULL OR u.username = CAST(:username AS text))
              AND (CAST(:state AS text) IS NULL OR s.current_state = CAST(:state AS text))
              AND (CAST(:createdDate AS date) IS NULL
                   OR (s.created_at AT TIME ZONE 'UTC')::date = CAST(:createdDate AS date))
            """, nativeQuery = true)
    List<Shipment> filterShipments(
            @Param("username") String username,
            @Param("state") String state,
            @Param("createdDate") LocalDate createdDate
    );
}
