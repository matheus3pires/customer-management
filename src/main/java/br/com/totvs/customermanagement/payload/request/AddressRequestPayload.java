package br.com.totvs.customermanagement.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for address data in customer creation or update requests")
public record AddressRequestPayload(

        @NotBlank
        @Size(min = 1, max = 255)
        @Schema(description = "Street name and number", example = "123 Main St")
        String street,

        @Size(max = 255)
        @Schema(description = "Additional address information", example = "Apartment 4B")
        String complement,

        @NotBlank
        @Size(min = 2, max = 100)
        @Schema(description = "City name", example = "Goiânia")
        String city,

        @NotBlank
        @Size(min = 2, max = 100)
        @Schema(description = "State name or abbreviation", example = "Goíás")
        String state,

        @NotBlank
        @Size(min = 5, max = 10)
        @Schema(description = "Postal or ZIP code", example = "74860-405")
        String zipCode
) {
}
