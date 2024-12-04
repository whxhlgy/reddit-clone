package me.project.backend.service;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.exception.CommunityAlreadyExistsException;
import me.project.backend.repository.CommunityRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommunityService {
    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public Community save(Community community) {
        try {
            log.info("save community: {}", community);
            return communityRepository.save(community);
        } catch (DataIntegrityViolationException e) {
            throw new CommunityAlreadyExistsException("This community already exists" + community.toString());
        }
        // validate if the community is existed
    }

    public List<Community> findAll() {
        return communityRepository.findAll();
    }

}
