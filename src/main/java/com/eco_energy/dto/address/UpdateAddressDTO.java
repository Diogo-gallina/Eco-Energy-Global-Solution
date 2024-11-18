package com.eco_energy.dto.address;

import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(
        Long id,

        @Size(max = 100, message = "Rua pode ter no máximo 100 caracteres")
        String street,

        Integer number,

        @Size(max = 8, message = "CEP pode ter no máximo 8 caracteres")
        String postalCode,

        Long customerId

) {

}
