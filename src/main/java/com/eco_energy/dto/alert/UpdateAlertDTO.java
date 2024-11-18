package com.eco_energy.dto.alert;

import com.eco_energy.model.enums.AlertLevel;
import jakarta.validation.constraints.Size;

public record UpdateAlertDTO(

        Long id,

        @Size(max = 100, message = "A mensagem do alerta pode ter no máximo 100 caracteres")
        String message,

        Boolean wasResolved,

        AlertLevel alertLevel

) {
}
