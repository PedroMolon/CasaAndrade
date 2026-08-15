package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.CategoryRequest;
import com.pedromolon.CasaAndrade.dto.response.CategoryResponse;
import com.pedromolon.CasaAndrade.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

}
