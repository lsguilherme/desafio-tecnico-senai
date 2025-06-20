package com.example.desafiosenai.controllers;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public void findProductById(@PathVariable Integer id) {
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.saveProduct(productRequestDto));
    }

    @PatchMapping("{id}")
    public void updateProductById(@PathVariable Integer id) {
    }

    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable Integer id) {
    }

    @PostMapping("{id}/restore")
    public void restoreProduct(@PathVariable Integer id) {
    }

    @PostMapping("{id}/discount/percent")
    public void discountPercentProduct(@PathVariable Integer id) {
    }

    @PostMapping("{id}/discount/coupon")
    public void discountCouponProduct(@PathVariable Integer id) {
    }

    @DeleteMapping("{id}/discount")
    public void deleteDiscount(@PathVariable Integer id) {
    }

}
