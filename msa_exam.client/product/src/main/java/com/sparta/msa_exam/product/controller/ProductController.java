package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Todo 헤더에 포트번호 실어야함
@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    @Value("${server.port}")
    private String serverPort;




    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    private <T> ResponseEntity<T> addServerPortHeader(T body, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", serverPort);
        return new ResponseEntity<>(body, headers, status);
    }


    @PostMapping
    public ResponseEntity<String>  addProducts(@RequestBody ProductRequestDto productDto) {
        productService.createProducts(productDto);
        return addServerPortHeader("Product added", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.findAllProducts();
        return addServerPortHeader(products, HttpStatus.OK);
    }


}
