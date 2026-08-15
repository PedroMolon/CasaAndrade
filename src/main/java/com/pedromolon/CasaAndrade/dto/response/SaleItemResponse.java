package com.pedromolon.CasaAndrade.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SaleItemResponse(
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
