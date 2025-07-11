package com.example.desafiosenai.controllers;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.PagedResponseDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.services.ProductService;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;


@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<ProductResponseDto>> findAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(defaultValue = "false") Boolean includeDeleted,
            @RequestParam(defaultValue = "false") Boolean onlyOutOfStock

    ) {
        PagedResponseDto<ProductResponseDto> productsPage = productService.findAllProducts(page, limit, search, minPrice, maxPrice, hasDiscount, sortBy, sortOrder, includeDeleted, onlyOutOfStock);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDto> findProductById(@PathVariable Integer id) {
        ProductResponseDto product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Void> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponse = productService.saveProduct(productRequestDto);
        URI location = URI.create("/products/" + productResponse.id());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping(path = "{id}", consumes = "application/json-patch+json")
    public ResponseEntity<ProductResponseDto> updateProductById(@PathVariable Integer id, @RequestBody JsonPatch patch) {
        ProductResponseDto product = productService.updateProduct(id, patch);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/restore")
    public ResponseEntity<ProductResponseDto> restoreProduct(@PathVariable Integer id) {
        ProductResponseDto product = productService.restoreProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("{id}/discount/percent")
    public ResponseEntity<ProductResponseDto> discountPercentProduct(@PathVariable Integer id, @RequestBody Map<String, BigDecimal> body) {
        BigDecimal percent = body.get("percent");

        ProductResponseDto product = productService.applyPercentDiscount(id, percent);
        return ResponseEntity.ok(product);
    }

    @PostMapping("{id}/discount/coupon")
    public ResponseEntity<ProductResponseDto> discountCouponProduct(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String code = body.get("code");

        ProductResponseDto product = productService.applyCouponToProduct(id, code);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}/discount")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Integer id) {
        productService.removeDiscountFromProduct(id);
        return ResponseEntity.noContent().build();
    }

}
