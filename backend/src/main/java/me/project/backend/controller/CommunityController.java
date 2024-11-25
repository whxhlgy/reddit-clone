package me.project.backend.controller;

import me.project.backend.domain.Community;
import me.project.backend.repository.CommunityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityRepository communityRepository;

    public CommunityController(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @GetMapping
    public List<Community> getAll() {
        return communityRepository.findAll();
    }

    @PostMapping
    public Community addCommunity(@RequestBody Community community) {
        communityRepository.save(community);
        return community;
    }
}
