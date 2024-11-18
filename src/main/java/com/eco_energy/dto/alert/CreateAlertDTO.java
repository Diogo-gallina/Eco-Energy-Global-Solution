package com.eco_energy.dto.alert;

import com.eco_energy.model.enums.AlertLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAlertDTO(

        @NotBlank(message = "A mensagem do alerta não pode ser vazia")
        @Size(max = 100, message = "A mensagem do alerta pode ter no máximo 100 caracteres")
        String message,

        @NotNull(message = "O status de resolução não pode ser nulo")
        Boolean wasResolved,

        @NotNull(message = "O nível do alerta não pode ser nulo")
        AlertLevel alertLevel,

        @NotNull(message = "O dispositivo associado não pode ser nulo")
        Long deviceId

) {
}
