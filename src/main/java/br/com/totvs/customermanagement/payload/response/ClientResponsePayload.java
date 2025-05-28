package br.com.totvs.customermanagement.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response payload representing a client")
public record ClientResponsePayload(

        @Schema(description = "Full name of the client", example = "Matheus Pires")
        String name,

        @Schema(description = "CPF of the client", example = "12345678901")
        String cpf,

        @Schema(description = "List of phone numbers associated with the client")
        List<PhoneResponsePayload> phones,

        @Schema(description = "List of addresses associated with the client")
        List<AddressResponsePayload> addresses
) {
}
