package com.sparta.msa_exam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private String name; // 주문 이름
    private List<Long> productIds; // 상품 ID 리스트
}