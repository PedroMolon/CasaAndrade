package com.pedromolon.CasaAndrade.controllerTest;

import com.pedromolon.CasaAndrade.controller.ProductController;
import com.pedromolon.CasaAndrade.dto.request.ProductRequest;
import com.pedromolon.CasaAndrade.dto.response.ProductResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private final ProductRequest request =
            new ProductRequest("Cimento", "Saco 50kg", new BigDecimal("35.00"), 100, 10, "img_url", 1L);

    private final ProductResponse response =
            new ProductResponse(1L, "Cimento", "Saco 50kg", new BigDecimal("35.00"), 100, 10, "img_url", "Materiais", false, true);

    @Test
    void shouldCreateProductAndReturn201() throws Exception {
        when(productService.saveNewProduct(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Cimento"));
    }

    @Test
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        ProductRequest invalid = new ProductRequest("", "desc", new BigDecimal("10"), 5, 1, "img_url", 1L);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        ProductRequest invalid = new ProductRequest("Produto", "desc", new BigDecimal("-5"), 5, 1, "img_url", 1L);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarProdutoPorIdERetornar200() throws Exception {
        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExiste() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/v1/products/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveListarProdutosPaginados() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);
        when(productService.getAllProducts(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Cimento"));
    }

    @Test
    void deveAtualizarProdutoERetornar200() throws Exception {
        when(productService.updateProduct(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cimento"));
    }

    @Test
    void deveDesativarProdutoERetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDesativarProdutoInexistente() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Product not found with id: 99"))
                .when(productService).disableProduct(99L);

        mockMvc.perform(delete("/api/v1/products/{id}", 99L))
                .andExpect(status().isNotFound());
    }

}
