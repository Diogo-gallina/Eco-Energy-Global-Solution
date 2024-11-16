package com.eco_energy.model;

import com.eco_energy.model.enums.UsageFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "JV_GS2_MVC_DEVICE")
@EntityListeners(AuditingEntityListener.class)
public class Device {

    @Id
    @GeneratedValue
    @Column(name = "device_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "electrical_power", nullable = false)
    private Integer electricalPower;

    @Column(name = "usage_frequency", nullable = false, length = 100)
    private UsageFrequency usageFrequency;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "device")
    private List<Alert> alerts;

    @OneToMany(mappedBy = "device")
    private List<EnergyConsumption> energyConsumptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
