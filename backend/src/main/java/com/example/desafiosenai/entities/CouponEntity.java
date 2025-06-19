package com.example.desafiosenai.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
public class CouponEntity extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private Boolean oneShot;

    @Column(nullable = false)
    private Integer maxUses;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    @OneToMany(mappedBy = "coupon")
    private List<ProductCouponApplicationEntity> applications;

    public CouponEntity() {
    }

    public CouponEntity(String code, CouponType type, BigDecimal discountValue, Boolean oneShot, Integer maxUses, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.code = code;
        this.type = type;
        this.discountValue = discountValue;
        this.oneShot = oneShot;
        this.maxUses = maxUses;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Boolean getOneShot() {
        return oneShot;
    }

    public void setOneShot(Boolean oneShot) {
        this.oneShot = oneShot;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
