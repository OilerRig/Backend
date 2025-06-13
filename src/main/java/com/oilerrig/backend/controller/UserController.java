package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.exception.AuthenticationAccessException;
import com.oilerrig.backend.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
class UserController {

    private final OrderService orderService;

    UserController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/users/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationAccessException("Authentication required");
        }
        else if (!Objects.equals(authentication.getName(), id)) {
            throw new AuthenticationAccessException("Authentication denied: user: " +  authentication.getName() + "cannot access orders for user: " +  id);
        }

        return ResponseEntity.ok().body(orderService.getAllOrdersByUser(id));
    }

    @GetMapping(value = "/admin/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }


}
