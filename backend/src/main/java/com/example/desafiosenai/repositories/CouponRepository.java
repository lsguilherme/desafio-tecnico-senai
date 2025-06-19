package com.example.desafiosenai.repositories;

import com.example.desafiosenai.entities.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Integer> {
}
