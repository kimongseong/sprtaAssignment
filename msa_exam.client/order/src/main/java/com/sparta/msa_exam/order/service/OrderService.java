package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.domain.Order;
import com.sparta.msa_exam.order.domain.OrderProduct;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.OrderProductResponseDto;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.feign.ProductClient;
import com.sparta.msa_exam.order.repository.OrderProductRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductClient productClient;

    public List<ProductResponseDto> getAllProducts(){
        return productClient.getAllProducts();
    }

    @Transactional
    public void createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.getName());

        List<ProductResponseDto> products = getAllProducts();
        List<Long> validProductIds = products.stream()
                .map(ProductResponseDto::getProductId)
                .toList();

        for (Long productId : orderRequestDto.getProductIds()) {
            if (!validProductIds.contains(productId)) {
                throw new NullPointerException("Product not found: " + productId);
            }
            OrderProduct orderProduct = OrderProduct.createOrderProduct(order, productId);
            order.addOrderProduct(orderProduct);
        }

        orderRepository.save(order);
    }


    protected Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

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

    private OrderProductResponseDto toOrderProductResponseDto(OrderProduct orderProduct) {
        return new OrderProductResponseDto(
                orderProduct.getProductId()
        );
    }

    @Transactional
    public OrderResponseDto updateOrder(long orderId, long productId) {
        Order order = findById(orderId);
        OrderProduct orderProduct = OrderProduct.createOrderProduct(order, productId);
        orderProductRepository.save(orderProduct);
        return toOrderResponseDto(order);
    }

    public OrderResponseDto getOrder(long orderId) {
        return toOrderResponseDto(findById(orderId));
    }
}
