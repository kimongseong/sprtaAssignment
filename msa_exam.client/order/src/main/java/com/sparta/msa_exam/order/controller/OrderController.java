package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        /*todo OrderRequestDto 의  product id 뽑아와서
           product 서비스로 보내서 product 가 존재하는지 확인하는 로직으로 변경
        */
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public void createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto);
    }


    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable long orderId) {
       return orderService.getOrder(orderId);
    }

    @PutMapping("/{orderId}")
    public OrderResponseDto addOrder(@PathVariable long orderId,@RequestBody long productId) {

            return orderService.updateOrder(orderId,productId);

    }

}
