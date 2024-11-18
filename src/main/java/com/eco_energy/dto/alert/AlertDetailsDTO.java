package com.eco_energy.dto.alert;

import com.eco_energy.model.Alert;
import com.eco_energy.model.enums.AlertLevel;

import java.time.LocalDateTime;

public record AlertDetailsDTO(
        Long alertId,
        String message,
        Boolean wasResolved,
        AlertLevel alertLevel,
        LocalDateTime createdAt,
        Long deviceId
) {

    public AlertDetailsDTO(Alert alert) {
        this(
                alert.getId(),
                alert.getMessage(),
                alert.getWasResolved(),
                alert.getAlertLevel(),
                alert.getCreatedAt(),
                alert.getDevice().getId()
        );
    }
}
