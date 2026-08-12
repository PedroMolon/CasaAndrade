package com.pedromolon.CasaAndrade.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name cannot be blank") String name,
        @NotBlank(message = "Email cannot be blank") @Email(message = "Email format is invalid") String email,
        @NotBlank(message = "Password cannot be blank") @Size(min = 6) String password
) {
}
