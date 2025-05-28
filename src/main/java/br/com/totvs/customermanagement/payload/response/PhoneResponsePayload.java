package br.com.totvs.customermanagement.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PhoneResponsePayload", description = "Response payload containing phone information")
public record PhoneResponsePayload(

        @Schema(description = "Phone number", example = "+5511999999999")
        String number
) {
}
