package io.github.nikolarakonjac.delivery_service_system.entity;

import io.github.nikolarakonjac.delivery_service_system.entity.enums.ShipmentState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "status_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentState state;

    @Column(name = "change_time", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private Instant changeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @PrePersist
    void onCreate() {
        if(changeTime == null){
            changeTime = Instant.now();
        }
    }

}
