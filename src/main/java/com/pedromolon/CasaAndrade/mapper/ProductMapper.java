package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(ProductRequest request);

    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);

}
