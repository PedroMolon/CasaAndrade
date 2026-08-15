package com.pedromolon.CasaAndrade.mapper;

import com.pedromolon.CasaAndrade.dto.request.ClientRequest;
import com.pedromolon.CasaAndrade.dto.response.ClientResponse;
import com.pedromolon.CasaAndrade.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(source = "personType", target = "personType")
    Client toEntity(ClientRequest request);

    ClientResponse toResponse(Client client);

}
