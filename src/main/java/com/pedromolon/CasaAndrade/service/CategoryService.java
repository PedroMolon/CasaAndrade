package com.pedromolon.CasaAndrade.service;

import com.pedromolon.CasaAndrade.dto.request.CategoryRequest;
import com.pedromolon.CasaAndrade.dto.response.CategoryResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.mapper.CategoryMapper;
import com.pedromolon.CasaAndrade.model.Category;
import com.pedromolon.CasaAndrade.model.Product;
import com.pedromolon.CasaAndrade.repository.CategoryRepository;
import com.pedromolon.CasaAndrade.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public CategoryResponse saveCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.name())) {
            throw new IllegalStateException("Category already exists");
        }

        return categoryMapper.toResponse(
                categoryRepository.save(categoryMapper.toEntity(request))
        );
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(request.name());

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with this id: " + id));

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                throw new IllegalStateException("Cannot delete category with linked products");
            }
        }

        categoryRepository.delete(category);
    }

}
