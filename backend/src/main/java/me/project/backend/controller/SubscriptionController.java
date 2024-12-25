package me.project.backend.controller;

import me.project.backend.domain.Subscription;
import me.project.backend.payload.dto.SubscriptionDTO;
import me.project.backend.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sub")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{communityName}")
    public SubscriptionDTO addSubscription(@PathVariable String communityName){
        return subscriptionService.subscribe(communityName);
    }
}
