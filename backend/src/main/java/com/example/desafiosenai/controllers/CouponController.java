package com.example.desafiosenai.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "coupons")
public class CouponController {

    @GetMapping
    public void findAllCoupons(){}

    @PostMapping
    public void createCoupon(){}

    @GetMapping("{code}")
    public void findCouponsByCode(@PathVariable String code){}

    @PatchMapping("{code}")
    public void updateCouponByCode(@PathVariable String code){}

    @DeleteMapping("{code}")
    public void deleteCoupon(@PathVariable String code){}
}
