package io.github.nikolarakonjac.delivery_service_system.repository;

import io.github.nikolarakonjac.delivery_service_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
