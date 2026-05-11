package net.f1v.kolexbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/")
    public String root() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
