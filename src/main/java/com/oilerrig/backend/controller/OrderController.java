package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OrderDto orderDto;
        if (authentication == null || !authentication.isAuthenticated()) {
            orderDto = orderService.addOrder(dto);
        }
        else {
            orderDto = orderService.addOrder(dto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@orderService.canAccessOrder(#id, authentication)")
    ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OrderDto orderDto;
        if (authentication == null || !authentication.isAuthenticated()) {
            orderDto = orderService.getOrder(id);
        }
        else {
            orderDto = orderService.getOrder(id, authentication.getName());
        }

        return ResponseEntity.ok().body(orderDto);
    }

}
