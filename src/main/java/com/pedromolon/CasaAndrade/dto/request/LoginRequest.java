package com.pedromolon.CasaAndrade.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank") @Email(message = "Email format is invalid") String email,
        @NotBlank(message = "Password cannot be blank") String password
) {
}
