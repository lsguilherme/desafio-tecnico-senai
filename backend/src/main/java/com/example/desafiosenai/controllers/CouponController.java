package com.example.desafiosenai.controllers;

import com.example.desafiosenai.dtos.requests.CouponRequestDto;
import com.example.desafiosenai.dtos.responses.CouponResponseDto;
import com.example.desafiosenai.services.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public void findAllCoupons(){}

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto){
        CouponResponseDto couponResponse = couponService.createCoupon(couponRequestDto);
        URI location = URI.create("/coupons/" + couponResponse.code());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{code}")
    public ResponseEntity<CouponResponseDto> findCouponsByCode(@PathVariable String code){
        CouponResponseDto coupon = couponService.findCouponByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @PatchMapping("{code}")
    public void updateCouponByCode(@PathVariable String code){}

    @DeleteMapping("{code}")
    public void deleteCoupon(@PathVariable String code){}
}
