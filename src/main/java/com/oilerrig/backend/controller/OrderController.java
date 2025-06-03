package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderEntityDto;
import com.oilerrig.backend.data.entity.OrderEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oilerrig.backend.data.dto.PlaceOrderRequestDto;

@RestController
class OrderController {

    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    OrderEntityDto placeOrder(@RequestBody PlaceOrderRequestDto dto) {


        return null;
    }

}
