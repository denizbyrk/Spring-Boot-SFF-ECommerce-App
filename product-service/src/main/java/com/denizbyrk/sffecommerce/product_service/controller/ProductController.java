package com.denizbyrk.sffecommerce.product_service.controller;

import com.denizbyrk.sffecommerce.product_service.entity.Category;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductRequestDTO;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.product_service.service.ProductService;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductUpdateRequestDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    ///
    /// create product
    ///
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request) {

        ProductResponseDTO response = this.productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    ///
    /// get product by id
    ///
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {

        ProductResponseDTO response = this.productService.getProduct(id);

        return ResponseEntity.ok(response);
    }

    ///
    /// get all active products
    ///
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponseDTO>> getAll() {

        List<ProductResponseDTO> products = this.productService.findAllProducts();

        return ResponseEntity.ok(products);
    }

    ///
    /// get logged in user's products
    ///
    @GetMapping("/my-products")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts() {

        List<ProductResponseDTO> products = this.productService.getMyProducts();

        return ResponseEntity.ok(products);
    }

    ///
    /// get products by seller username
    ///
    @GetMapping("/seller/{username}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySeller(@PathVariable String username) {

        List<ProductResponseDTO> products = this.productService.getProductsBySeller(username);

        return ResponseEntity.ok(products);
    }

    ///
    /// get products by category
    ///
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getByCategory(@PathVariable Category category) {

        List<ProductResponseDTO> products = this.productService.getByCategory(category);

        return ResponseEntity.ok(products);
    }

    ///
    /// update product
    ///
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequestDTO request) {

        ProductResponseDTO response = this.productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }

    ///
    /// remove product
    ///
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable Long id) {

        String response = this.productService.removeProduct(id);

        return ResponseEntity.ok(response);
    }
}