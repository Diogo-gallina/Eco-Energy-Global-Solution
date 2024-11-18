package com.eco_energy.dto.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDeviceDTO(

        @NotBlank(message = "O nome do dispositivo não pode ser vazio")
        @Size(max = 100, message = "O nome do dispositivo pode ter no máximo 100 caracteres")
        String name,

        @NotNull(message = "A potência elétrica não pode ser nula")
        Integer electricalPower,

        @NotNull(message = "A frequência de uso não pode ser nula")
        com.eco_energy.model.enums.UsageFrequency usageFrequency,

        @NotNull(message = "O ID do cliente não pode ser nulo")
        Long customerId
) {}
