package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public void createProducts(ProductRequestDto productRequestDto) {
        Product product = Product.addProduct(productRequestDto);
        productRepository.save(product);
    }

    public List<ProductResponseDto> findAllProducts() {

        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toProductResponseDto)
                .collect(Collectors.toList());

    }

    private ProductResponseDto toProductResponseDto(Product product) {

        return new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getSupply_price()
        );

    }


}
