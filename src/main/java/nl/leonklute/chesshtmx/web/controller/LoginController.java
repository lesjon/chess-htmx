package nl.leonklute.chesshtmx.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping
    public void getUser() {
    }

    @PostMapping
    public void createUser() {
    }

}
