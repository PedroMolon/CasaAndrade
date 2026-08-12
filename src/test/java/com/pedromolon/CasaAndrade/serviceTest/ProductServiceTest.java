package com.pedromolon.CasaAndrade.serviceTest;

import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.mapper.ProductMapper;
import com.pedromolon.CasaAndrade.model.Category;
import com.pedromolon.CasaAndrade.model.Product;
import com.pedromolon.CasaAndrade.repository.CategoryRepository;
import com.pedromolon.CasaAndrade.repository.ProductRepository;
import com.pedromolon.CasaAndrade.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Materiais");

        product = new Product();
        product.setId(1L);
        product.setName("Cimento");
        product.setDescription("Saco 50kg");
        product.setPrice(new BigDecimal("35.00"));
        product.setQuantity(100);
        product.setMinQuantity(5);
        product.setCategory(category);
        product.setActive(true);

        request = new ProductRequest("Cimento", "Saco 50kg", new BigDecimal("35.00"), 100, 10, null, 1L);
        response = new ProductResponse(1L, "Cimento", "Saco 50kg", new BigDecimal("35.00"), 100, 10, null, "Materiais", false, true);
    }

    @Nested
    @DisplayName("Save Product Test")
    class SaveProductTest {

        @Test
        @DisplayName("Should save product successfully")
        void shouldSaveProductSuccessfully() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(productMapper.toEntity(request)).thenReturn(product);
            when(productRepository.save(product)).thenReturn(product);
            when(productMapper.toResponse(product)).thenReturn(response);

            ProductResponse productResponse = productService.saveNewProduct(request);

            assertThat(productResponse).isNotNull().isEqualTo(response);
            verify(productRepository, times(1)).save(product);
        }

        @Test
        @DisplayName("Should throw exception when save product without category")
        void shouldThrowExceptionWhenSaveProductWithOutCategory() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.saveNewProduct(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category not found with this id: " + request.categoryId());

            verify(productRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("Get All Products Test")
    class GetAllProductsTest {

        @Test
        @DisplayName("Should return paginated products successfully")
        void shouldReturnPaginatedProductsSuccessfully() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

            when(productRepository.findByActiveTrue(pageable)).thenReturn(productPage);
            when(productMapper.toResponse(product)).thenReturn(response);

            Page<ProductResponse> result = productService.getAllProducts(pageable);

            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst()).isEqualTo(response);
            verify(productRepository, times(1)).findByActiveTrue(pageable);
        }
    }

    @Nested
    @DisplayName("Get Product Test")
    class GetProductTest {

        @Test
        @DisplayName("Should return product by id successfully")
        void shouldReturnProductByIdSuccessfully() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productMapper.toResponse(product)).thenReturn(response);

            ProductResponse productResponse = productService.getProductById(1L);

            assertThat(productResponse).isEqualTo(response);
            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when product id do not exists")
        void shouldThrowExceptionWhenProductIdDoNotExists() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }

    }

    @Nested
    @DisplayName("Update Product Test")
    class UpdateProductTest {

        @Test
        @DisplayName("Should update product successfully")
        void shouldUpdateProductSuccessfully() {
            ProductRequest requestAtualizado = new ProductRequest("Cimento CPV", "Nova descrição", new BigDecimal("38.00"), 90, 10, null, 1L);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(productMapper.toResponse(product)).thenReturn(response);

            productService.updateProduct(1L, requestAtualizado);

            verify(productRepository, times(1)).save(product);
            assertThat(product.getName()).isEqualTo("Cimento CPV");
            assertThat(product.getPrice()).isEqualTo(new BigDecimal("38.00"));
        }

        @Test
        @DisplayName("Should throw exception when update product that not exists")
        void shouldThrowExceptionWhenProductNotExists() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(99L, request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(productRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when update product that category not exists")
        void shouldThrowExceptionWhenUpdateProductThatCategoryNotExists() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.updateProduct(1L, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Category");

            verify(productRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("Disable Product Test")
    class DisableProductTest {

        @Test
        @DisplayName("Should disable product successfully")
        void shouldDisableProductSuccessfully() {
            product.setActive(true);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(product)).thenReturn(product);

            productService.disableProduct(1L);

            assertThat(product.getActive()).isFalse();
            verify(productRepository, times(1)).save(product);
        }

        @Test
        @DisplayName("Should throw exception when disable product that not exists")
        void shouldThrowExceptionWhenDisableProductThatNotExists() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.disableProduct(99L))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(productRepository, never()).save(any());
        }

    }

}
