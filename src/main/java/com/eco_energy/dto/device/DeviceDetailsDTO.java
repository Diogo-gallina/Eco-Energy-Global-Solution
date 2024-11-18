package com.eco_energy.dto.device;

import com.eco_energy.model.Device;
import com.eco_energy.model.enums.UsageFrequency;

import java.time.LocalDateTime;
import java.util.List;

public record DeviceDetailsDTO(

        Long deviceId,
        String name,
        Integer electricalPower,
        UsageFrequency usageFrequency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long customerId,
        List<Long> alertIds,
        List<Long> energyConsumptionIds

) {

    public DeviceDetailsDTO(Device device) {
        this(
                device.getId(),
                device.getName(),
                device.getElectricalPower(),
                device.getUsageFrequency(),
                device.getCreatedAt(),
                device.getUpdatedAt(),
                device.getCustomer().getId(),
                device.getAlerts().stream().map(alert -> alert.getId()).toList(),
                device.getEnergyConsumptions().stream().map(consumption -> consumption.getId()).toList()
        );
    }
}