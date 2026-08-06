package com.pedromolon.CasaAndrade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name cannot be blank") String name,
        @NotBlank(message = "Description cannot be blank") String description,
        @NotNull(message = "Price cannot be null") @Positive(message = "Price must be positive") BigDecimal price,
        @NotNull(message = "Quantity cannot be null") @Positive(message = "Quantity must be positive") Integer quantity,
        @NotNull(message = "Min quantity cannot be null") @Positive(message = "Min quantity must be positive") Integer minQuantity,
        @NotBlank(message = "Image url cannot be blank") String imgUrl,
        @NotNull(message = "Category id cannot be null") Long categoryId
) {
}
