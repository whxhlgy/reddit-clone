package me.project.backend.controller;

import me.project.backend.payload.dto.UserDTO;
import me.project.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/whoami")
    public UserDTO whoami() {
        return userService.whoami();
    }
}
