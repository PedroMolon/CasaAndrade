package com.pedromolon.CasaAndrade.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SaleResponse (
    Long id,
    String clientName,
    String sellerName,
    BigDecimal total,
    LocalDateTime saleDate,
    List<SaleItemResponse> items
) {
}