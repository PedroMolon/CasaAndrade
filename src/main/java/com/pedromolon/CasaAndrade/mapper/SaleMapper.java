package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.SaleRequest;
import com.pedromolon.CasaAndrade.dto.response.SaleResponse;
import com.pedromolon.CasaAndrade.model.Sale;
import jakarta.persistence.Id;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SaleItemMapper.class)
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "saleDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "client.id", source = "clientId")
    Sale toEntity(SaleRequest saleRequest);

    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "user.name", target = "sellerName")
    SaleResponse toResponse(Sale sale);

}
