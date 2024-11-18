package com.eco_energy.dto.energyConsumption;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateEnergyConsumptionDTO(

        @NotNull(message = "O tempo de uso não pode ser nulo")
        LocalDateTime usageTime,

        @NotNull(message = "O consumo em kWh não pode ser nulo")
        Double kwhConsumption,

        @NotNull(message = "O custo da energia não pode ser nulo")
        Double energyCost,

        @NotNull(message = "O ID do dispositivo não pode ser nulo")
        Long deviceId
) { }
