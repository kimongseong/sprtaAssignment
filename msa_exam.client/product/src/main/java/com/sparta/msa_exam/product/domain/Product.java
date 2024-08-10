package com.sparta.msa_exam.product.domain;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private int supply_price;


    public static Product addProduct(ProductRequestDto productRequestDto) {
        return builder()
                .name(productRequestDto.getName())
                .supply_price(productRequestDto.getSupply_price())
                .build();
    }


}
