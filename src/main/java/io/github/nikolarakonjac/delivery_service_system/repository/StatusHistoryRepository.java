package io.github.nikolarakonjac.delivery_service_system.repository;

import io.github.nikolarakonjac.delivery_service_system.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
}
