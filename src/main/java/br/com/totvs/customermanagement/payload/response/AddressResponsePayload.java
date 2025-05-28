package br.com.totvs.customermanagement.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload representing an address")
public record AddressResponsePayload(

        @Schema(description = "Street name and number", example = "123 Main St")
        String street,

        @Schema(description = "Additional address information", example = "Apartment 4B")
        String complement,

        @Schema(description = "City name", example = "Goiânia")
        String city,

        @Schema(description = "State name or abbreviation", example = "Goíás")
        String state,

        @Schema(description = "Postal or ZIP code", example = "74860-405")
        String zipCode
) {
}
