package com.example.desafiosenai.controllers;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void findAllProducts() {
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

    @PatchMapping("{id}")
    public void updateProductById(@PathVariable Integer id) {
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
    public void discountPercentProduct(@PathVariable Integer id) {
    }

    @PostMapping("{id}/discount/coupon")
    public ResponseEntity<ProductResponseDto> discountCouponProduct(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String code = body.get("code");

        ProductResponseDto product = productService.applyCouponToProduct(id, code);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("{id}/discount")
    public void deleteDiscount(@PathVariable Integer id) {
    }

}
