package com.genesis.wms.service;

import com.genesis.wms.dto.ProductRequest;
import com.genesis.wms.dto.ProductResponse;
import com.genesis.wms.entity.Product;
import com.genesis.wms.exception.DuplicateSkuException;
import com.genesis.wms.exception.ResourceNotFoundException;
import com.genesis.wms.repository.InventoryRepository;
import com.genesis.wms.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new DuplicateSkuException("Product with SKU '" + request.sku() + "' already exists");
        }
        Product product = Product.builder()
                .sku(request.sku())
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .unitPrice(request.unitPrice())
                .build();
        return toResponse(productRepository.save(product));
    }

    public ProductResponse getProductBySku(String sku) {
        return toResponse(findBySku(sku));
    }

    public List<ProductResponse> listAll(String category) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(p -> category == null || category.equalsIgnoreCase(p.getCategory()))
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse updateProduct(String sku, ProductRequest request) {
        Product product = findBySku(sku);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(request.category());
        product.setUnitPrice(request.unitPrice());
        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(String sku) {
        Product product = findBySku(sku);
        inventoryRepository.findByProduct_Sku(sku)
                .ifPresent(inventoryRepository::delete);
        productRepository.delete(product);
    }

    public Product findBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getUnitPrice(),
                product.getCreatedAt()
        );
    }
}