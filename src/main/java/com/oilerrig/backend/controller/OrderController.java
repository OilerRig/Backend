package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
class OrderController {

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(dto));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderService.getOrder(id));
    }

}
