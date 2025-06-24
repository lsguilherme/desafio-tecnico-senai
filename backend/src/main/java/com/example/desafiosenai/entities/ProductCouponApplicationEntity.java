package com.example.desafiosenai.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_coupon_applications")
public class ProductCouponApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    private LocalDateTime appliedAt;

    private LocalDateTime removedAt;

    public ProductCouponApplicationEntity() {
    }

    public ProductCouponApplicationEntity(ProductEntity product, CouponEntity coupon, LocalDateTime appliedAt, LocalDateTime removedAt) {
        this.product = product;
        this.coupon = coupon;
        this.appliedAt = appliedAt;
        this.removedAt = removedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }
}
