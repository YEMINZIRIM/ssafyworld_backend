package com.yeminjilim.ssafyworld.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/token")
@RestController
public class JWTController {

    @GetMapping
    public ResponseEntity<Void> authentication() {
        return ResponseEntity.ok().build();
    }
}
