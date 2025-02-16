package nl.leonklute.chesshtmx.controller;

import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(Model model) {
        model.addAttribute("rating", 100);
        return "user";
    }

    @GetMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) {
        model.addAttribute("rating", 100);
        return "user";
    }

    @PostMapping
    public String createUser(@RequestParam String username, Model model) {
        UserEntity user = userService.createUser(username);
        model.addAttribute("user", user);
        return "user";
    }
}
