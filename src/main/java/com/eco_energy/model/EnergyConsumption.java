package com.eco_energy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "JV_GS2_MVC_ENERGY_CONSUMPTION")
@EntityListeners(AuditingEntityListener.class)
public class EnergyConsumption {

    @Id
    @GeneratedValue
    @Column(name = "energy_consumption_id")
    private Long id;

    @Column(name = "usage_time", nullable = false)
    private LocalDateTime usageTime;

    @Column(name = "kwh_consumption", nullable = false)
    private Double kwhConsumption;

    @Column(name = "energy_cost", nullable = false)
    private Double energyCost;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

}
