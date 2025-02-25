package nl.leonklute.chesshtmx.controller;

import lombok.extern.slf4j.Slf4j;
import nl.leonklute.chesshtmx.db.model.UserEntity;
import nl.leonklute.chesshtmx.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUser(Principal principal, Model model) {
        try {
            var user = (UserEntity) userService.loadUserByUsername(principal.getName());
            model.addAttribute("rating", user.getRating());
        } catch (
                UsernameNotFoundException e) {
            log.error("Could not find user: ", e);
        }
        return "user";
    }

    @GetMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) {
        try {
            var user = (UserEntity) userService.loadUserByUsername(username);
            model.addAttribute("rating", user.getRating());
        } catch (
                UsernameNotFoundException e) {
            log.error("Could not find user: ", e);
        }
        return "user";
    }

    @PostMapping
    public String createUser(@RequestParam String username, @RequestParam String password, Model model) {
        log.info("createUser({},", username);
        UserEntity user = userService.createUser(username, password);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/search")
    public String findUser(@RequestParam String search, Model model) {
        model.addAttribute("userSearchResults", userService.findUsers(search));
        return "user :: search-results";
    }
}
