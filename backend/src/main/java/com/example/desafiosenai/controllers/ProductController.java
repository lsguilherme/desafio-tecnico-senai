    package com.example.desafiosenai.controllers;

    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("products")
    public class ProductController {

        @GetMapping
        public void findAllProducts(){}

        @GetMapping("{id}")
        public  void findProductById(@PathVariable Integer id){}

        @PostMapping
        public void saveProduct(){}

        @PatchMapping("{id}")
        public void updateProductById(@PathVariable Integer id){}

        @DeleteMapping("{id}")
        public void deleteProduct(@PathVariable Integer id){}

        @PostMapping("{id}/restore")
        public void restoreProduct(@PathVariable Integer id){}

        @PostMapping("{id}/discount/percent")
        public void discountPercentProduct(@PathVariable Integer id){}

        @PostMapping("{id}/discount/coupon")
        public void discountCouponProduct(@PathVariable Integer id){}

        @DeleteMapping("{id}/discount")
        public void deleteDiscount(@PathVariable Integer id){}

    }
