package com.eco_energy.dto.energyConsumption;

import java.time.LocalTime;

public record UpdateEnergyConsumptionDTO(
        Long id,
        LocalTime usageTime,
        Double kwhConsumption,
        Double energyCost,
        Long deviceId
) { }
