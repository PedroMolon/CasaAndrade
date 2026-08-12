package com.pedromolon.CasaAndrade.dto.response;

public record LoginResponse(
        String token,
        Long expiresIn
) {
}
