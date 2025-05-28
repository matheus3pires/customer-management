package br.com.totvs.customermanagement.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Payload to create or update a client")
public record ClientRequestPayload(

        @NotBlank
        @Size(min = 10, max = 255)
        @Schema(description = "Full name of the client", example = "Matheus Pires")
        String name,

        @NotBlank
        @Size(min=11, max=11)
        @Pattern(regexp = "\\d{11}", message = "CPF must have exactly 11 digits")
        @Schema(description = "CPF of the client", example = "12345678901")
        String cpf,

        @NotEmpty
        @Schema(description = "List of phone numbers associated with the client", minLength = 1)
        List<PhoneRequestPayload> phones,

        @NotEmpty
        @Schema(description = "List of addresses associated with the client", minLength = 1)
        List<AddressRequestPayload> addresses
) {
}
