package com.oilerrig.backend.controller;

import com.oilerrig.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public String test() {
        return "Server is up and running.";
    }

    @GetMapping("/servicebus/{message}")
    public String testServiceBus(@PathVariable String message) {
        testService.pushToLogQueue(message);
        return "Message pushed to servicebus";
    }

    @GetMapping("/auth/roles")
    public ResponseEntity<String> getAuthorities() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return ResponseEntity.ok().body("Recieved Authorities: " + authorities);
    }

    @GetMapping("/auth")
    public ResponseEntity<String> getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok().body("Recieved Authentication: " + authentication);
    }
}
