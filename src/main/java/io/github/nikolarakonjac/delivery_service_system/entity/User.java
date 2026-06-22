package io.github.nikolarakonjac.delivery_service_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Shipment> shipments = new ArrayList<>();

    public void addShipment(Shipment newShipment){
        shipments.add(newShipment);
        newShipment.setUser(this);
    }

    public void removeShipment(Shipment shipment){
        shipments.remove(shipment);
        shipment.setUser(null);
    }
}
