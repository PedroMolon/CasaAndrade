package com.pedromolon.CasaAndrade.dto.request;

import com.pedromolon.CasaAndrade.model.enums.PersonType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequest(
        @NotNull(message = "Person type is required") PersonType type,
        @NotBlank(message = "Name cannot be blank") String name,
        @NotBlank(message = "Document cannot be blank") String document,
        @Email(message = "Email is invalid") String email,
        String phone
) {

    @AssertTrue(message =
        "Document length does not match the person type (CPF or CNPJ)"
    )
    public boolean isDocumentValid() {
        if (type == null || document == null) {
            return true;
        }
        int length = document.replaceAll("\\D", "").length();
        return (type == PersonType.PF && length == 11)
                || (type == PersonType.PJ && length == 14);
    }

}
