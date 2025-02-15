package nl.leonklute.chesshtmx.controller;

import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/login")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Principal getUser(Model model) {
        return (Principal) model.getAttribute("user");
    }

    @PostMapping
    public String createUser(@RequestParam String username, Model model) {
        UserEntity user = userService.createUser(username);
        model.addAttribute("user", user);
        return "user";
    }
}
