package com.eco_energy.dto.energyConsumption;

import java.time.LocalDateTime;

public record UpdateEnergyConsumptionDTO(
        Long id,
        LocalDateTime usageTime,
        Double kwhConsumption,
        Double energyCost,
        Long deviceId
) { }
