package com.pedromolon.CasaAndrade.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Name cannot be blank") String name
) {
}
