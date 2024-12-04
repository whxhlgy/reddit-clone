package me.project.backend;

import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.domain.User;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.service.CommunityService;
import me.project.backend.service.PostService;
import me.project.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    private final CommunityService communityService;
    private final PostService postService;
    private final UserService userService;

    public BackendApplication(CommunityService communityService, PostService postService, UserService userService) {
        this.communityService = communityService;
        this.postService = postService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        communityService.save(new Community("webdev", "test_desc"));
        Post post = new Post();
        post.setContent("test");
        post.setTitle("test title");
        post.setUsername("anonymous");
        post.setLikesCount(100L);
        postService.save(post);
        Post post2 = new Post();
        post2.setContent("test2");
        post2.setTitle("test title2");
        post2.setUsername("anonymous");
        post2.setLikesCount(100L);
        postService.save(post2);

        // create a user for test
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        userService.save(user);
    }
}
