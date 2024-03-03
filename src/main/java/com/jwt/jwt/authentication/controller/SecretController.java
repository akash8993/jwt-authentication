package com.jwt.jwt.authentication.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/secret")
public class SecretController{

    @GetMapping("/")
    public ResponseEntity<String> getSecret()
    {
        return ResponseEntity.status(HttpStatus.OK).body(UUID.randomUUID().toString());

    }

    @GetMapping("/csrf")
    public CsrfToken getCsrf(HttpServletRequest httpServletRequest)
    {
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
}
