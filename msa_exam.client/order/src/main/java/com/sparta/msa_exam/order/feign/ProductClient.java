package com.sparta.msa_exam.order.feign;

import com.sparta.msa_exam.order.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {
    @GetMapping("/products")
    List<ProductResponseDto> getAllProducts();

}

