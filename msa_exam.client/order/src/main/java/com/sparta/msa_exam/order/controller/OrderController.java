package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*todo OrderRequestDto 의  product id 뽑아와서
   product 서비스로 보내서 product 가 존재하는지 확인하는 로직으로 변경
*/
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Value("${server.port}")
    private String serverPort;

    private <T> ResponseEntity<T> addServerPortHeader(T body, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", serverPort);
        return new ResponseEntity<>(body, headers, status);
    }


    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        orderService.createOrder(orderRequestDto);
        return addServerPortHeader("ok", HttpStatus.CREATED);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("orderId") long orderId) {

        return addServerPortHeader(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> addOrder(@PathVariable("orderId") long orderId, @RequestBody long productId) {

        return addServerPortHeader(orderService.updateOrder(orderId, productId), HttpStatus.OK);

    }

}
