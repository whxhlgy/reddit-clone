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

import java.util.ArrayList;
import java.util.List;

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
        // Community webdev = communityService.save(new Community("webdev",
        // "test_desc"));
        // // create a user for test
        // User _user = new User();
        // _user.setUsername("test");
        // _user.setPassword("test");
        // User user = userService.save(_user);
        //
        // List<Post> posts = new ArrayList<>();
        // for (int i = 0; i < 100_000; i++) {
        // // create post for
        // Post post = new Post();
        // post.setContent("Content " + i);
        // post.setTitle("title " + i);
        // post.setUser(user);
        // post.setCommunity(webdev);
        // posts.add(post);
        // }
        // List<Post> savedPosts = postService.saveAll(posts);
        //
        // for (Post post : savedPosts) {
        // CommentDTO c1 = commentService.save(post.getId(), null, new CommentDTO("I
        // think it is right", "test"));
        // CommentDTO c2 = commentService.save(post.getId(), c1.getId(), new
        // CommentDTO("I agree with you", "test2"));
        // CommentDTO c3 = commentService.save(post.getId(), c1.getId(), new
        // CommentDTO("I agree with you too", "test3"));
        //
        // CommentDTO c4 = commentService.save(post.getId(), c2.getId(), new
        // CommentDTO("I think you are wrong", "test4"));
        // CommentDTO c5 = commentService.save(post.getId(), c4.getId(), new
        // CommentDTO("Why do you think so?", "test5"));
        // CommentDTO c6 = commentService.save(post.getId(), c5.getId(), new
        // CommentDTO("I have the same question", "test6"));
        //
        // CommentDTO c7 = commentService.save(post.getId(), c6.getId(), new
        // CommentDTO("Can you explain more?", "test7"));
        // CommentDTO c8 = commentService.save(post.getId(), c6.getId(), new
        // CommentDTO("I don't understand", "test8"));
        //
        // CommentDTO c9 = commentService.save(post.getId(), c5.getId(), new
        // CommentDTO("Please clarify", "test9"));
        // CommentDTO c10 = commentService.save(post.getId(), c5.getId(), new
        // CommentDTO("I need more details", "test10"));
        //
        // CommentDTO c11 = commentService.save(post.getId(), c8.getId(), new
        // CommentDTO("What do you mean?", "test11"));
        // CommentDTO c12 = commentService.save(post.getId(), c8.getId(), new
        // CommentDTO("Can you give an example?", "test12"));
        // }
    }
}
