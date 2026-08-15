package com.pedromolon.CasaAndrade.controller;

import com.pedromolon.CasaAndrade.dto.request.SaleRequest;
import com.pedromolon.CasaAndrade.dto.response.SaleResponse;
import com.pedromolon.CasaAndrade.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(saleService.getSaleById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getByClientId(Long clientId, Pageable pageable) {
        return ResponseEntity.ok().body(saleService.getByClient(clientId, pageable));
    }

    @PostMapping
    public ResponseEntity<SaleResponse> save(@Valid @RequestBody SaleRequest request) {
        SaleResponse response = saleService.createSale(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

}
