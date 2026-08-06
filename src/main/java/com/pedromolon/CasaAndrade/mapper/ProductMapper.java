package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

}
