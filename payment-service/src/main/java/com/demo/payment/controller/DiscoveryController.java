package com.demo.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class DiscoveryController {

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong at " + new Date();
    }
}
