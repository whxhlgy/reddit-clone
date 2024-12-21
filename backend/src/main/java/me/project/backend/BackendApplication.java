package me.project.backend;

import me.project.backend.domain.Comment;
import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.domain.User;
import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.service.CommentService;
import me.project.backend.service.CommunityService;
import me.project.backend.service.PostService;
import me.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    private final CommunityService communityService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    public BackendApplication(CommunityService communityService, PostService postService, CommentService commentService,
            UserService userService) {
        this.communityService = communityService;
        this.postService = postService;
        this.commentService = commentService;
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
        PostDTO p2 = postService.save(post2);

        CommentDTO c1 = commentService.save(post2.getId(), null, new CommentDTO("I think it is right", "test"));
        CommentDTO c2 = commentService.save(post2.getId(), c1.getId(), new CommentDTO("I agree with you", "test2"));
        CommentDTO c3 = commentService.save(post2.getId(), c1.getId(), new CommentDTO("I agree with you too", "test3"));

        CommentDTO c4 = commentService.save(post2.getId(), c2.getId(), new CommentDTO("I think you are wrong", "test4"));
        CommentDTO c5 = commentService.save(post2.getId(), c4.getId(), new CommentDTO("Why do you think so?", "test5"));
        CommentDTO c6 = commentService.save(post2.getId(), c5.getId(), new CommentDTO("I have the same question", "test6"));

        CommentDTO c7 = commentService.save(post2.getId(), c6.getId(), new CommentDTO("Can you explain more?", "test7"));
        CommentDTO c8 = commentService.save(post2.getId(), c6.getId(), new CommentDTO("I don't understand", "test8"));

        CommentDTO c9 = commentService.save(post2.getId(), c5.getId(), new CommentDTO("Please clarify", "test9"));
        CommentDTO c10 = commentService.save(post2.getId(), c5.getId(), new CommentDTO("I need more details", "test10"));

        CommentDTO c11 = commentService.save(post2.getId(), c8.getId(), new CommentDTO("What do you mean?", "test11"));
        CommentDTO c12 = commentService.save(post2.getId(), c8.getId(), new CommentDTO("Can you give an example?", "test12"));

        // create a user for test
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        userService.save(user);
    }
}
