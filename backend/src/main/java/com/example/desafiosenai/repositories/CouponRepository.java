package com.example.desafiosenai.repositories;

import com.example.desafiosenai.entities.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {

    boolean existsByCode(String normalizedCode);

    Optional<CouponEntity> findByCode(String normalized);
}
