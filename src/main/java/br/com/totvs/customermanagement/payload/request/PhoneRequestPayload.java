package br.com.totvs.customermanagement.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload representing a phone number")
public record PhoneRequestPayload(

        @NotBlank
        @Size(max = 20)
        @Schema(description = "Phone number", example = "+55 11 91234-5678")
        String number
) {
}
