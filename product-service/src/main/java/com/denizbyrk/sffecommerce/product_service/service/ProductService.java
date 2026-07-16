package com.denizbyrk.sffecommerce.product_service.service;

import com.denizbyrk.sffecommerce.product_service.entity.Product;
import com.denizbyrk.sffecommerce.product_service.entity.Category;
import com.denizbyrk.sffecommerce.product_service.client.UserClient;
import com.denizbyrk.sffecommerce.product_service.DTO.UserResponseDTO;
import com.denizbyrk.sffecommerce.product_service.entity.ProductStatus;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductRequestDTO;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductResponseDTO;
import com.denizbyrk.sffecommerce.product_service.DTO.ProductUpdateRequestDTO;
import com.denizbyrk.sffecommerce.product_service.event.ProductEventPublisher;
import com.denizbyrk.sffecommerce.product_service.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductEventPublisher productEventPublisher;

    public ProductService(ProductRepository productRepository, UserClient userClient, ProductEventPublisher productEventPublisher) {

        this.productRepository = productRepository;
        this.userClient = userClient;
        this.productEventPublisher = productEventPublisher;
    }

    ///
    /// create product
    ///
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true),
            @CacheEvict(value = "productsBySeller", allEntries = true)
    })
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO user = this.userClient.getUserByUsername(username);

        Product product = new Product();
        product.setName(request.getName());
        product.setSellerId(user.getId());
        product.setSellerName(username);
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product savedProduct = this.productRepository.save(product);

        this.productEventPublisher.sendProductCreatedEvent(savedProduct.getId());

        return this.mapToDTO(savedProduct);
    }

    ///
    /// get product by id
    ///
    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProduct(Long id) {

        Product product = this.productRepository.findById(id).orElseThrow();

        return this.mapToDTO(product);
    }

    ///
    /// get logged in user's all products
    ///
    @Cacheable(value = "myProducts", key = "authentication.name")
    public List<ProductResponseDTO> getMyProducts() {

        List<ProductResponseDTO> response = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO currentUser = this.userClient.getUserByUsername(username);

        List<Product> products = this.productRepository.findBySellerId(currentUser.getId());

        for(Product product : products) {

            ProductResponseDTO productResponseDTO = this.mapToDTO(product);

            response.add(productResponseDTO);
        }

        return response;
    }

    ///
    /// get all products by seller
    ///
    @Cacheable(value = "productsBySeller", key = "#username")
    public List<ProductResponseDTO> getProductsBySeller(String username) {

        List<Product> products = this.productRepository.findBySellerName(username);
        List<ProductResponseDTO> response = new ArrayList<>();

        for(Product product : products) {

            if (product.getProductStatus() == ProductStatus.ACTIVE) {

                ProductResponseDTO productResponseDTO = this.mapToDTO(product);

                response.add(productResponseDTO);
            }
        }

        return response;
    }

    ///
    /// get products by category
    ///
    @Cacheable(value = "productsByCategory", key = "#category")
    public List<ProductResponseDTO> getByCategory(Category category) {

        List<Product> products = this.productRepository.findByCategory(category);
        List<ProductResponseDTO> response = new ArrayList<>();

        for (Product product : products) {

            if (product.getProductStatus() == ProductStatus.ACTIVE) {

                ProductResponseDTO productResponseDTO = this.mapToDTO(product);

                response.add(productResponseDTO);
            }
        }

        return response;
    }

    ///
    /// remove products
    ///
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#id"),
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsBySeller", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    public String removeProduct(Long id) {

        Product product = this.productRepository.findById(id).orElseThrow();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO currentUser = this.userClient.getUserByUsername(username);

        if (!product.getSellerId().equals(currentUser.getId())) {

            throw new AccessDeniedException("You are not the owner of this product");
        }

        product.setProductStatus(ProductStatus.INACTIVE);

        this.productRepository.save(product);

        return "Product is no longer available.";
    }

    ///
    /// get all products
    ///
    @Cacheable(value = "products")
    public List<ProductResponseDTO> findAllProducts() {

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getProductStatus() == ProductStatus.ACTIVE)
                .map(this::mapToDTO)
                .toList();
    }

    ///
    /// update product
    ///
    @Caching(evict = {
            @CacheEvict(value = "product", key = "#id"),
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "productsBySeller", allEntries = true),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    public ProductResponseDTO updateProduct(Long id, ProductUpdateRequestDTO request) {

        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO currentUser = this.userClient.getUserByUsername(username);

        if (!product.getSellerId().equals(currentUser.getId())) {

            throw new AccessDeniedException("You are not the owner of this product.");
        }

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product updatedProduct = this.productRepository.save(product);

        return mapToDTO(updatedProduct);
    }

    private ProductResponseDTO mapToDTO(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getDescription(),
                product.getSellerName()
        );
    }
}