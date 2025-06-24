package com.example.desafiosenai.entities;

import com.example.desafiosenai.dtos.DiscountDto;
import com.example.desafiosenai.dtos.responses.ProductResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "products")
public class ProductEntity extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCouponApplicationEntity> productCouponApplications;

    public ProductEntity() {
        this.productCouponApplications = new ArrayList<>();
    }

    public ProductEntity(String name, String description, BigDecimal price, Integer stock) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<ProductCouponApplicationEntity> getProductCouponApplications() {
        return productCouponApplications;
    }

    public void setProductCouponApplications(List<ProductCouponApplicationEntity> productCouponApplications) {
        this.productCouponApplications = productCouponApplications;
    }


    private Optional<ProductCouponApplicationEntity> getActiveCoupon() {
        return this.productCouponApplications.stream()
                .filter(pc -> pc.getRemovedAt() == null &&
                        pc.getCoupon() != null &&
                        pc.getCoupon().getValidFrom().isBefore(LocalDateTime.now()) &&
                        pc.getCoupon().getValidUntil().isAfter(LocalDateTime.now()))
                .findFirst();
    }

    private BigDecimal getFinalPrice() {
        BigDecimal calculatedPrice = this.price;
        Optional<ProductCouponApplicationEntity> activeApplication = getActiveCoupon();

        if (activeApplication.isPresent()) {
            CouponEntity coupon = activeApplication.get().getCoupon();
            if (coupon.getType() == CouponType.PERCENT) {
                BigDecimal discountPercentage = coupon.getDiscountValue().divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);
                calculatedPrice = this.price.multiply(BigDecimal.ONE.subtract(discountPercentage));
            } else if (coupon.getType() == CouponType.FIXED) {
                calculatedPrice = this.price.subtract(coupon.getDiscountValue());
                if (calculatedPrice.compareTo(BigDecimal.ZERO) < 0) {
                    calculatedPrice = BigDecimal.ZERO;
                }
            }
        }
        return calculatedPrice;
    }

    public DiscountDto getActiveDiscountDto() {
        Optional<ProductCouponApplicationEntity> activeApplication = getActiveCoupon();
        if (activeApplication.isPresent()) {
            CouponEntity coupon = activeApplication.get().getCoupon();
            return new DiscountDto(
                    coupon.getType(),
                    coupon.getDiscountValue(),
                    activeApplication.get().getAppliedAt()
            );
        }
        return null;
    }

    public ProductResponseDto toDto() {
        DiscountDto discount = getActiveDiscountDto();
        BigDecimal finalPrice = getFinalPrice();
        boolean hasCoupon = discount != null;
        boolean isOutOfStock = this.stock <= 0;

        return new ProductResponseDto(
                this.id,
                this.name,
                this.description,
                this.stock,
                isOutOfStock,
                this.price,
                finalPrice,
                discount,
                hasCoupon,
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }
}
