package com.eco_energy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalTime usageTime;

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

    public EnergyConsumption(LocalTime usageTime, Device device) {
        this.usageTime = usageTime;
        this.kwhConsumption = this.calculateKwhConsumption(device);
        this.energyCost = this.calculateEnergyCost(this.kwhConsumption);
        this.createdAt = LocalDateTime.now();
        this.device = device;
        device.getEnergyConsumptions().add(this);
    }

    public void setKwhConsumption(Device device) {
        this.kwhConsumption = this.calculateKwhConsumption(device);
    }

    public void setEnergyCost(Double kwhConsumption) {
        this.energyCost = calculateEnergyCost(kwhConsumption);
    }


    private Double calculateKwhConsumption(Device device) {
        Integer power = device.getElectricalPower();
        Double hoursUsed = usageTime.getHour() + (usageTime.getMinute() / 60.0);
        return (power * hoursUsed) / 1000.0;
    }

    private Double calculateEnergyCost(Double kwhConsumption) {
        final double DISTRIBUTION_TARIFF_30 = 95.98;
        final double ENERGY_TARIFF_30 = 89.39;

        final double DISTRIBUTION_TARIFF_100 = 164.54;
        final double ENERGY_TARIFF_100 = 153.23;

        final double DISTRIBUTION_TARIFF_220 = 246.82;
        final double ENERGY_TARIFF_220 = 229.85;

        final double DISTRIBUTION_TARIFF_ABOVE_220 = 274.24;
        final double ENERGY_TARIFF_ABOVE_220 = 255.39;

        if (kwhConsumption <= 30) {
            double ratePerKwh = (DISTRIBUTION_TARIFF_30 + ENERGY_TARIFF_30) / 100.0;
            return kwhConsumption * ratePerKwh;
        } else if (kwhConsumption <= 100) {
            double ratePerKwh = (DISTRIBUTION_TARIFF_100 + ENERGY_TARIFF_100) / 100.0;
            return kwhConsumption * ratePerKwh;
        } else if (kwhConsumption <= 220) {
            double ratePerKwh = (DISTRIBUTION_TARIFF_220 + ENERGY_TARIFF_220) / 100.0;
            return kwhConsumption * ratePerKwh;
        } else {
            double ratePerKwh = (DISTRIBUTION_TARIFF_ABOVE_220 + ENERGY_TARIFF_ABOVE_220) / 100.0;
            return kwhConsumption * ratePerKwh;
        }
    }

}
