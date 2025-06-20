package com.example.desafiosenai.services;

import com.example.desafiosenai.dtos.requests.ProductRequestDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.example.desafiosenai.entities.ProductEntity;
import com.example.desafiosenai.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto saveProduct(ProductRequestDto productRequestDto) {
        String normalizedName = productRequestDto.name().replaceAll("\\s+", " ").trim();

        if (productRepository.findByName(normalizedName).isPresent()){
            throw new IllegalArgumentException("Já existe um produto com esse nome.");
        };

        ProductEntity product = new ProductEntity();
        product.setName(normalizedName);
        product.setDescription(productRequestDto.description());
        product.setStock(productRequestDto.stock());
        product.setPrice(productRequestDto.price());

        var savedProduct = productRepository.save(product);
        return savedProduct.toDto();

    }
}
