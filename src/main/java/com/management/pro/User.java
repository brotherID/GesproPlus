package com.management.pro;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {

    @GetMapping("/user")
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + "CHECK_CV" + "\")")
    public String getName() {
        return "John Doe";
    }
}
