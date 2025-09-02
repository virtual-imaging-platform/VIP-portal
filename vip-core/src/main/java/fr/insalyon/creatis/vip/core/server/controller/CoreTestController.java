package fr.insalyon.creatis.vip.core.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreTestController {
    @GetMapping("/test")
    public String testCall() {
        return "hello core\n";
    }
}
