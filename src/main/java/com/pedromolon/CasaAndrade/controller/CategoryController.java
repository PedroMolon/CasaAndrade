package com.pedromolon.CasaAndrade.controller;

import com.pedromolon.CasaAndrade.dto.request.CategoryRequest;
import com.pedromolon.CasaAndrade.dto.response.CategoryResponse;
import com.pedromolon.CasaAndrade.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @GetMapping("${categoryId}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.saveCategory(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok().body(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
