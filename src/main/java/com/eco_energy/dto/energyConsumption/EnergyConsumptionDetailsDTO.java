package com.eco_energy.dto.energyConsumption;

import com.eco_energy.model.EnergyConsumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record EnergyConsumptionDetailsDTO(

        Long energyConsumptionId,
        LocalTime usageTime,
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