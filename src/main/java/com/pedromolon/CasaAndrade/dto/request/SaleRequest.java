package com.pedromolon.CasaAndrade.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleRequest(
        @NotNull(message = "Client id is required") Long clientId,
        @NotEmpty(message = "Sale list must have at least one item") List<SaleItemRequest> items
) {
}
