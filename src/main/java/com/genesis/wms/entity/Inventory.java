package com.genesis.wms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "warehouse_location")
    private String warehouseLocation;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @PreUpdate
    @PrePersist
    protected void onUpdate() {
        this.lastUpdated = Instant.now();
    }
}