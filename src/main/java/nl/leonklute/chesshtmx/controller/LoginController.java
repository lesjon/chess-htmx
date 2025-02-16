package nl.leonklute.chesshtmx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public void getUser() {
    }

    @PostMapping
    public void createUser() {
    }

}
