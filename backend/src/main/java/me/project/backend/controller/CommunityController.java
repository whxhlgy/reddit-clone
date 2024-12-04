package me.project.backend.controller;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.service.CommunityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communities")
@Slf4j
public class CommunityController {
    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public List<Community> findAll() {
        log.info("find all communities");
        return communityService.findAll();
    }

    @PostMapping
    public Community save(@RequestBody Community community) {
        return communityService.save(community);
    }
}
