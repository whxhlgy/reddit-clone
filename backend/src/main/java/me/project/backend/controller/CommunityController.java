package me.project.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.payload.dto.CommunityDTO;
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
    public List<Community> findAll(HttpServletRequest request) {
        return communityService.findAll();
    }

    @GetMapping("/name/{communityName}")
    public CommunityDTO findByName(@PathVariable String communityName) {
        return communityService.findByName(communityName);
    }

    @PostMapping
    public Community save(@RequestBody Community community) {
        return communityService.save(community);
    }
}
