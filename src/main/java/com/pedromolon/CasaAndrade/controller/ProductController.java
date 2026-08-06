package com.pedromolon.CasaAndrade.controller;

import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long productId) {
        return ResponseEntity.ok().body(productService.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.saveNewProduct(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long productId, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok().body(productService.updateProduct(productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> disable(@PathVariable Long productId) {
        productService.disableProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
