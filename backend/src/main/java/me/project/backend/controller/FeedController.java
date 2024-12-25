package me.project.backend.controller;

import me.project.backend.payload.dto.PostDTO;
import me.project.backend.service.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/timeline/{username}")
    public List<PostDTO> timeline(@PathVariable String username){
        return feedService.getUserTimeLine(username);
    }
}
