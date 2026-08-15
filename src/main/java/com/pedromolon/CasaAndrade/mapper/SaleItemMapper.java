package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.SaleItemRequest;
import com.pedromolon.CasaAndrade.dto.response.SaleItemResponse;
import com.pedromolon.CasaAndrade.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    SaleItem toEntity(SaleItemRequest request);

    @Mapping(source = "product.name", target = "productName")
    SaleItemResponse toResponse(SaleItem saleItem);

}
