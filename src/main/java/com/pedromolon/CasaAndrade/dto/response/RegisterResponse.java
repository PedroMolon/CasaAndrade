package com.pedromolon.CasaAndrade.dto.response;

import com.pedromolon.CasaAndrade.model.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RegisterResponse(
        Long id,
        String name,
        String email,
        String password,
        Set<Role> roles,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
