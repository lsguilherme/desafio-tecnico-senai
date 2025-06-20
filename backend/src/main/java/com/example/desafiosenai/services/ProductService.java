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

    public void deleteProductById(Integer id) {
        ProductEntity product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));


        product.softDelete();

        productRepository.save(product);
    }

    public ProductResponseDto restoreProductById(Integer id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (product.getDeletedAt() == null){
            throw new IllegalArgumentException("Produto já está ativo.");
        }

        product.restore();

        return productRepository.save(product).toDto();
    }
}
