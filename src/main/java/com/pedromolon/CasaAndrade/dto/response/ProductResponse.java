package com.pedromolon.CasaAndrade.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Integer minQuantity,
        String imgUrl,
        String categoryName,
        Boolean lowQuantity,
        Boolean active
) {
}
