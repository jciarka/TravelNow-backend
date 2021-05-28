package com.travelnow.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @GetMapping
    public String home() {
        return "<h1>Welcome</h1>"; }

    @GetMapping("/user")
    public String user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return "<h1>Welcome User" + currentPrincipalName + "</h1>";
    }

    @GetMapping("/admin")
    public String admin() {
        return "<h1>Welcome Admin</h1>"; }

}
