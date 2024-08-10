package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // #args[?] = ? 번째 인자를 키로 사용한다
    // #result.~~~~~ = 리턴값이나 인자값의

    @CachePut(value = "productCache", key = "#result.productId")
    @CacheEvict(cacheNames = "productsCache", allEntries = true)
    @Transactional
    public ProductResponseDto createProducts(ProductRequestDto productRequestDto) {

        Product product = Product.addProduct(productRequestDto);

        productRepository.save(product);
        return  toProductResponseDto(product);
    }

    //@Cacheable: 이 메서드의 결과는 캐시가능
    //cacheNames : config 에서 어떤 규칙을 적용할지 지정하기 위한 이름 , 해당 메서드로 인해 만들어질 캐시를 지칭하는이름
    //key: 캐시 데이터를 구분하기 위해 활용하는값
    @Cacheable(cacheNames = "productsCache" , key = "methodName")
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
