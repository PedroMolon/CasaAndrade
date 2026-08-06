package com.pedromolon.CasaAndrade.service;

import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.mapper.ProductMapper;
import com.pedromolon.CasaAndrade.model.Category;
import com.pedromolon.CasaAndrade.model.Product;
import com.pedromolon.CasaAndrade.repository.CategoryRepository;
import com.pedromolon.CasaAndrade.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse saveNewProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this id: " + request.categoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional(readOnly= true)
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable).map(productMapper::toResponse);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setMinQuantity(request.minQuantity());
        product.setImgUrl(request.imgUrl());
        product.setCategory(categoryRepository.findById(request.categoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found with this id")));

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void disableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (Boolean.TRUE.equals(product.getActive()) && product.getQuantity() != null && product.getQuantity() <= 0) {
            product.setActive(false);
            productRepository.save(product);
        } else {
            throw new IllegalStateException("Product cannot be disable. It must be active and have 0 stock");
        }
    }

}
