package com.oilerrig.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final String message = "Server is up and running";

    @GetMapping("/test/{print}")
    public String test(@PathVariable String print) {
        return message + " " + print + ".";
    }

    @GetMapping("/test/")
    public String test() {
        return message + ".";
    }
}
