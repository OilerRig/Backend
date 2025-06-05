package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.service.ProductService;
import com.oilerrig.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable UUID id) {
        return ResponseEntity.ok().body(userService.getUserOrders(id));
    }


}
