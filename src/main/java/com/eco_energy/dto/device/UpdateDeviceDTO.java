package com.eco_energy.dto.device;

import com.eco_energy.model.enums.UsageFrequency;
import jakarta.validation.constraints.Size;

public record UpdateDeviceDTO(

        Long id,

        @Size(max = 100, message = "O nome do dispositivo pode ter no máximo 100 caracteres")
        String name,
        Integer electricalPower,
        UsageFrequency usageFrequency
) {}
