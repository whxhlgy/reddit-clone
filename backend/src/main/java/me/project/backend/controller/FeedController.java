package me.project.backend.controller;

import me.project.backend.payload.dto.PostDTO;
import me.project.backend.service.FeedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/timeline/{username}")
    public List<PostDTO> timeline(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        return feedService.getUserTimeLine(username, page, size);
    }
}
