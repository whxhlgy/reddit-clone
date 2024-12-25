package me.project.backend.controller;

import me.project.backend.payload.dto.CommunityDTO;
import me.project.backend.payload.dto.UserDTO;
import me.project.backend.service.SubscriptionService;
import me.project.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/whoami")
    public UserDTO whoami() {
        return userService.whoami();
    }

    @GetMapping("/name/{username}/sub")
    public List<CommunityDTO> getSubscription(@PathVariable String username) {
        return subscriptionService.getSubscriptionsByUserId(username);
    }
}
