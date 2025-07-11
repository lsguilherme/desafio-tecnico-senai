package com.example.desafiosenai.repositories;

import com.example.desafiosenai.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findByName(String normalizedName);

    Optional<ProductEntity> findByIdAndDeletedAtIsNull(Integer id);
}
