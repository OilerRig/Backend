package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
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

    @GetMapping(value = "/user/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getUserOrders(@PathVariable String auth0_id) {
        return ResponseEntity.ok().body("not implemented yet");
    }


}
