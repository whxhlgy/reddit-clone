package me.project.backend;

import me.project.backend.domain.Community;
import me.project.backend.repository.CommunityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    private final CommunityRepository communityRepository;

    public BackendApplication(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        communityRepository.save(new Community("webdev", "test_desc"));
    }
}
