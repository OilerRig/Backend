package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.exception.ValidityException;
import com.oilerrig.backend.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
class UserController {

    private final OrderService orderService;

    UserController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || !(auth instanceof JwtAuthenticationToken)) {
            throw new ValidityException("Invalid authentication");
        }

        Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
        String auth0_id = jwt.getSubject();

        List<OrderDto> orders = orderService.getAllOrdersByUser(auth0_id);
        return ResponseEntity.ok().body(orders);
    }


}
