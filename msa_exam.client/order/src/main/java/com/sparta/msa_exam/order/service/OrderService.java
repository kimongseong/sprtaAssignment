package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderProduct;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.feign.ProductClient;
import com.sparta.msa_exam.order.repository.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    // 모든 제품을 가져오는 메서드
    private List<ProductResponseDto> getAllProducts() {
        return productClient.getAllProducts();
    }

    // 유효한 제품 ID 집합을 가져오는 메서드
    private Set<Long> getValidProductIds() {
        return getAllProducts().stream()
                .map(ProductResponseDto::getProductId)
                .collect(Collectors.toSet());
    }

    // 제품 ID 유효성 검증
    private void validateProductIds(List<Long> productIds) {
        Set<Long> validProductIds = getValidProductIds();
        List<Long> invalidProductIds = productIds.stream()
                .filter(productId -> !validProductIds.contains(productId))
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            throw new IllegalArgumentException("다음 제품이 존재하지 않습니다: " + invalidProductIds);
        }
    }

    // 주문 ID로 주문 찾기
    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문 ID " + orderId + "를 찾을 수 없습니다"));
    }


    // Order를 OrderResponseDto로 변환
    private OrderResponseDto toOrderResponseDto(Order order) {
        List<OrderProductResponseDto> orderProductDtos = order.getOrderProducts().stream()
                .map(this::toOrderProductResponseDto)
                .collect(Collectors.toList());

        return new OrderResponseDto(
                order.getOrderId(),
                order.getName(),
                orderProductDtos
        );
    }
    // OrderProduct를 OrderProductResponseDto로 변환
    private OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct) {
        return new OrderProductResponseDto(orderProduct.getProductId());
    }

    // 주문 조회
    public OrderResponseDto getOrder(long orderId) {
        Order order = findById(orderId);
        return toOrderResponseDto(order);
    }

    // 주문 생성
    @Transactional
    public void createOrder(OrderRequestDto orderRequestDto) {
        validateProductIds(orderRequestDto.getProductIds());

        Order order = new Order(orderRequestDto.getName());
        for (Long productId : orderRequestDto.getProductIds()) {
            OrderProduct orderProduct = OrderProduct.createOrderProduct(order, productId);
            order.addOrderProduct(orderProduct);
        }

        orderRepository.save(order);
    }

    // 주문 업데이트
    @Transactional
    public OrderResponseDto updateOrder(long orderId, long productId) {
        Order order = findById(orderId);
        validateProductIds(List.of(productId));

        OrderProduct orderProduct = OrderProduct.createOrderProduct(order, productId);
        order.addOrderProduct(orderProduct);

        orderRepository.save(order);

        return toOrderResponseDto(order);
    }
}