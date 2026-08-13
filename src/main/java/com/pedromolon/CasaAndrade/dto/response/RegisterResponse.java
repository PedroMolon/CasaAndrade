package com.pedromolon.CasaAndrade.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(
        Long id,
        String name,
        String email,
        String message
) {
}
