package com.oilerrig.backend.controller;

import com.oilerrig.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test/")
    public String test() {
        return "Server is up and running.";
    }

    @GetMapping("/test/servicebus/{message}")
    public String testServiceBus(@PathVariable String message) {
        testService.pushToLogQueue(message);
        return "Message pushed to servicebus";
    }
}
