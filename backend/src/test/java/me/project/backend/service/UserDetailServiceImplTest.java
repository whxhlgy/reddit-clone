package me.project.backend.service;

import me.project.backend.domain.User;
import me.project.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @InjectMocks
    UserDetailServiceImpl userDetailServiceImpl;

    @Mock
    UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        when(userRepository.findByUsername("junjiezh")).thenReturn(Optional.of(new User("junjiezh", "password")));
        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername("junjiezh");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("junjiezh");
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }
}