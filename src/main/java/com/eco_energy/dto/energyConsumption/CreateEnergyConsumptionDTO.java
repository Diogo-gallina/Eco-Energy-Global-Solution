package com.eco_energy.dto.energyConsumption;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateEnergyConsumptionDTO(

        @NotNull(message = "O tempo de uso não pode ser nulo")
        LocalTime usageTime,

        @NotNull(message = "O ID do dispositivo não pode ser nulo")
        Long deviceId
) { }
