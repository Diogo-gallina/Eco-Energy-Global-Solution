package com.eco_energy.dto.energyConsumption;

import com.eco_energy.model.EnergyConsumption;

import java.time.LocalDateTime;

public record EnergyConsumptionDetailsDTO(

        Long energyConsumptionId,
        LocalDateTime usageTime,
        Double kwhConsumption,
        Double energyCost,
        LocalDateTime createdAt,
        Long deviceId

) {

    public EnergyConsumptionDetailsDTO(EnergyConsumption energyConsumption) {
        this(
                energyConsumption.getId(),
                energyConsumption.getUsageTime(),
                energyConsumption.getKwhConsumption(),
                energyConsumption.getEnergyCost(),
                energyConsumption.getCreatedAt(),
                energyConsumption.getDevice().getId()
        );
    }
}