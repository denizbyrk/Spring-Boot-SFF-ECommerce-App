package com.denizbyrk.sffecommerce.product_service;

import com.denizbyrk.sffecommerce.product_service.util.JwtUtil;
import com.denizbyrk.sffecommerce.product_service.entity.Category;
import com.denizbyrk.sffecommerce.product_service.service.ProductService;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.product_service.controller.ProductController;

import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void createProduct_shouldReturnCreated() throws Exception {

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Laptop")
                        .category(Category.Electronics)
                        .price(1500.0)
                        .description("Gaming laptop")
                        .sellerUsername("deniz")
                        .build();


        when(this.productService.createProduct(any())).thenReturn(response);

        this.mockMvc.perform(
                        post("/api/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "name": "Laptop",
                            "category": "ELECTRONICS",
                            "description": "Gaming laptop",
                            "price": 1500
                        }
                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(5L)
                        .name("Phone")
                        .price(900.0)
                        .build();

        when(this.productService.getProduct(5L)).thenReturn(response);

        this.mockMvc.perform(
                        get("/api/product/5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Phone"));

    }

    @Test
    void getAllProducts_shouldReturnList() throws Exception {

        ProductResponseDTO product =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Laptop")
                        .build();

        when(this.productService.findAllProducts()).thenReturn(List.of(product));

        this.mockMvc.perform(
                        get("/api/product/getAll")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void removeProduct_shouldReturnMessage() throws Exception {

        when(this.productService.removeProduct(1L))
                .thenReturn("Product is no longer available.");

        this.mockMvc.perform(
                        delete("/api/product/1")
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Product is no longer available."));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {

        ProductResponseDTO response =
                ProductResponseDTO.builder()
                        .id(1L)
                        .name("Updated Laptop")
                        .price(2000.0)
                        .build();

        when(this.productService.updateProduct(any(), any())).thenReturn(response);

        this.mockMvc.perform(
                        put("/api/product/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "name":"Updated Laptop",
                            "category":"ELECTRONICS",
                            "description":"Updated",
                            "price":2000
                        }
                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Updated Laptop"));
    }
}