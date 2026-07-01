package lk.workbridge.marketplace.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/custom")

public class CustomController {

    @GetMapping("/test-session")
    public String testSessionIdAPI(){
        return "Session validate ";
    }
}
