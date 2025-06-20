package com.example.desafiosenai.repositories;

import com.example.desafiosenai.entities.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Optional<ProductEntity> findByName(String normalizedName);

    Optional<ProductEntity> findByIdAndDeletedAtIsNull(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE ProductEntity p SET p.deletedAt = :deletedAt WHERE p.id = :id AND p.deletedAt IS NULL")
    int softDeleteById(@Param("id") Integer id, @Param("deletedAt") LocalDateTime deletedAt);
}
