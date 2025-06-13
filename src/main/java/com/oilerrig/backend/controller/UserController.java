package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController("/users")
class UserController {

    private final OrderService orderService;

    UserController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@orderService.canAccessUserOrders(#id, authentication)")
    ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable String id) {
        List<OrderDto> orders = orderService.getAllOrdersByUser(id);
        return ResponseEntity.ok().body(orders);
    }


}
