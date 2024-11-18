package com.eco_energy.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAddressDTO(

        @NotBlank(message = "Rua não pode ser vazia")
        @Size(max = 100, message = "Rua pode ter no máximo 100 caracteres")
        String street,

        @NotNull(message = "Número não pode ser vazio")
        Integer number,

        @NotBlank(message = "CEP não pode ser vazio")
        @Size(min = 8, max = 8, message = "CEP deve conter 8 caracteres")
        String postalCode,

        @NotNull(message = "ID do cliente não pode ser nulo")
        Long customerId

) {
}
