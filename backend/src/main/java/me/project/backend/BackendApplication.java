package me.project.backend;

import me.project.backend.mapper.CommunityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    private final CommunityMapper communityMapper;

    public BackendApplication(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(this.communityMapper.findById("1"));
    }
}
