package com.pedromolon.CasaAndrade.dto.response;

import com.pedromolon.CasaAndrade.model.enums.PersonType;
import lombok.Builder;

@Builder
public record ClientResponse(
        Long id,
        PersonType personType,
        String name,
        String document,
        String email,
        String phone,
        boolean active
) {
}
