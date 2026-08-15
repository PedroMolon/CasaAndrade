package com.pedromolon.CasaAndrade.serviceTest;

import com.pedromolon.CasaAndrade.dto.request.CategoryRequest;
import com.pedromolon.CasaAndrade.dto.response.CategoryResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.mapper.CategoryMapper;
import com.pedromolon.CasaAndrade.model.Category;
import com.pedromolon.CasaAndrade.repository.CategoryRepository;
import com.pedromolon.CasaAndrade.repository.ProductRepository;
import com.pedromolon.CasaAndrade.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequest request;
    private CategoryResponse response;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Materiais");

        request = new CategoryRequest("Materiais");
        response = new CategoryResponse(1L, "Materiais");
    }

    @Nested
    @DisplayName("Save Category Test")
    class SaveCategoryTest {

        @Test
        @DisplayName("Should save category successfully")
        void shouldSaveCategorySuccessfully() {
            when(categoryRepository.existsByName("Materiais")).thenReturn(false);
            when(categoryMapper.toEntity(request)).thenReturn(category);
            when(categoryRepository.save(category)).thenReturn(category);
            when(categoryMapper.toResponse(category)).thenReturn(response);

            CategoryResponse categoryResponse = categoryService.saveCategory(request);

            assertThat(categoryResponse).isNotNull().isEqualTo(response);
            verify(categoryRepository, times(1)).save(category);
        }

        @Test
        @DisplayName("Should throw exception when category name already exists")
        void shouldThrowExceptionWhenCategoryNameAlreadyExists() {
            when(categoryRepository.existsByName("Materiais")).thenReturn(true);

            assertThatThrownBy(() -> categoryService.saveCategory(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Category already exists");

            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Category Test")
    class GetCategoryTest {

        @Test
        @DisplayName("Should return category by id successfully")
        void shouldReturnCategoryByIdSuccessfully() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(categoryMapper.toResponse(category)).thenReturn(response);

            CategoryResponse categoryResponse = categoryService.getCategoryById(1L);

            assertThat(categoryResponse).isEqualTo(response);
            verify(categoryRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when category id does not exist")
        void shouldThrowExceptionWhenCategoryIdDoesNotExist() {
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.getCategoryById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("Update Category Test")
    class UpdateCategoryTest {

        @Test
        @DisplayName("Should update category successfully")
        void shouldUpdateCategorySuccessfully() {
            CategoryRequest requestAtualizado = new CategoryRequest("Materiais de Construção");
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toResponse(category)).thenReturn(response);

            categoryService.updateCategory(1L, requestAtualizado);

            verify(categoryRepository, times(1)).save(category);
            assertThat(category.getName()).isEqualTo("Materiais de Construção");
        }

        @Test
        @DisplayName("Should throw exception when updating category that does not exist")
        void shouldThrowExceptionWhenUpdatingCategoryThatDoesNotExist() {
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.updateCategory(99L, request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Category Test")
    class DeleteCategoryTest {

        @Test
        @DisplayName("Should delete category successfully when no products are linked")
        void shouldDeleteCategorySuccessfully() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

            categoryService.deleteCategory(1L);

            verify(categoryRepository, times(1)).delete(category);
        }

        @Test
        @DisplayName("Should throw exception when deleting category that does not exist")
        void shouldThrowExceptionWhenDeletingCategoryThatDoesNotExist() {
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoryService.deleteCategory(99L))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(categoryRepository, never()).delete(any());
        }

    }
}