package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Subscription;
import me.project.backend.domain.User;
import me.project.backend.exception.ServiceRuntimeException;
import me.project.backend.exception.SubscriptionAlreadyExistsException;
import me.project.backend.exception.notFound.CommunityNotFoundException;
import me.project.backend.exception.notFound.UserNotFoundException;
import me.project.backend.payload.dto.CommunityDTO;
import me.project.backend.payload.dto.SubscriptionDTO;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.repository.SubscriptionRepository;
import me.project.backend.repository.UserRepository;
import me.project.backend.util.ContextUtil;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final ModelMapper modelMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, CommunityRepository communityRepository, ModelMapper modelMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.modelMapper = modelMapper;
    }

    public List<CommunityDTO> getSubscriptionsByUserId(String username) {
        List<Subscription> subs = subscriptionRepository.findAllWithCommunityByUserId(username);
        return subs.stream().map((subscription -> {
            Community community = subscription.getCommunity();
            return modelMapper.map(community, CommunityDTO.class);
        })).toList();
    }

    public SubscriptionDTO subscribe(String communityName) {
        String username = ContextUtil.getUsername().orElseThrow(() -> new ServiceRuntimeException("cannot get username"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        Community community = communityRepository.findByName(communityName).orElseThrow(() -> new CommunityNotFoundException("cannot get community"));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCommunity(community);
        try {
            Subscription save = subscriptionRepository.save(subscription);
            return new SubscriptionDTO(save.getId(), save.getUser().getUsername(), save.getCommunity().getName());
        } catch (DataIntegrityViolationException e) {
            throw new SubscriptionAlreadyExistsException("you have already subscribed it");
        }
    }
}
