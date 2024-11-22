package com.eco_energy.dto.energyConsumption;

import java.time.LocalTime;

public record UpdateEnergyConsumptionDTO(
        Long id,
        LocalTime usageTime,
        Long deviceId
) { }
