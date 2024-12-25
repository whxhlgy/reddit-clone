package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.exception.CommunityAlreadyExistsException;
import me.project.backend.exception.notFound.CommunityNotFoundException;
import me.project.backend.payload.dto.CommunityDTO;
import me.project.backend.repository.CommunityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final ModelMapper modelMapper;
    private final SubscriptionService subscriptionService;

    public CommunityService(CommunityRepository communityRepository, ModelMapper modelMapper, SubscriptionService subscriptionService) {
        this.communityRepository = communityRepository;
        this.modelMapper = modelMapper;
        this.subscriptionService = subscriptionService;
    }

    public Community save(Community community) {
        try {
            log.info("save community: {}", community);
            Community save = communityRepository.save(community);
            subscriptionService.subscribe(save.getName());
            return save;
        } catch (DataIntegrityViolationException e) {
            throw new CommunityAlreadyExistsException("This community already exists" + community.toString());
        }
        // validate if the community is existed
    }

    public List<Community> findAll() {
        return communityRepository.findAll();
    }

    public CommunityDTO findByName(String name) {
        Community community = communityRepository.findByName(name).orElseThrow(() -> new CommunityNotFoundException(name));
        return modelMapper.map(community, CommunityDTO.class);
    }

}
