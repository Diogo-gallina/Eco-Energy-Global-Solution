package com.eco_energy.dto.address;

import com.eco_energy.model.Address;

import java.time.LocalDateTime;

public record AddressDetailsDTO(
        Long id,
        String street,
        Integer number,
        String postalCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String customerName
) {

    public AddressDetailsDTO(Address address) {
        this(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getPostalCode(),
                address.getCreatedAt(),
                address.getUpdatedAt(),
                address.getCustomer() != null ? address.getCustomer().getUsername() : null
        );
    }
}
