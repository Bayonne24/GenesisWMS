package com.genesis.wms.controller;

import com.genesis.wms.dto.ProductRequest;
import com.genesis.wms.dto.ProductResponse;
import com.genesis.wms.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/{sku}")
    @Operation(summary = "Get product by SKU")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @GetMapping
    @Operation(summary = "List all products, optionally filter by category")
    public ResponseEntity<List<ProductResponse>> listProducts(
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.listAll(category));
    }

    @PutMapping("/{sku}")
    @Operation(summary = "Update a product by SKU")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(sku, request));
    }

    @DeleteMapping("/{sku}")
    @Operation(summary = "Delete a product by SKU")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        productService.deleteProduct(sku);
        return ResponseEntity.noContent().build();
    }
}