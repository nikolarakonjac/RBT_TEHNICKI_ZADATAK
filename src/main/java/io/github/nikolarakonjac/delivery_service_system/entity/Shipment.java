package io.github.nikolarakonjac.delivery_service_system.entity;


import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "tracker_id", columnDefinition = "UUID")
    private UUID trackerId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "current_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentState currentState;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StatusHistory> statusHistory = new ArrayList<>();

    public void addStatusHistory(StatusHistory newStatusHistory) {
        this.statusHistory.add(newStatusHistory);
        newStatusHistory.setShipment(this);
    }

    public void removeStatusHistory(StatusHistory statusHistory) {
        this.statusHistory.remove(statusHistory);
        statusHistory.setShipment(null);
    }

    @PrePersist
    void onCreate() {
        if(createdAt == null){
            createdAt = Instant.now();
        }

        if(currentState == null){
            currentState = ShipmentState.CREATED;
        }

        if(statusHistory.isEmpty()){
            StatusHistory newStatusHistory = StatusHistory.builder()
                    .state(ShipmentState.CREATED)
                    .note(null)
                    .build();

            this.addStatusHistory(newStatusHistory);
        }
    }
}
