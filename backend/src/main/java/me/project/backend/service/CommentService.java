package me.project.backend.service;

import me.project.backend.domain.Comment;
import me.project.backend.domain.Post;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;

    public CommentService(ModelMapper modelMapper, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    public List<CommentDTO> findAll(int postId) {
        Post post = postRepository.findPostWithCommentsById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<Comment> comments = post.getComments();
        return modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {}.getType());
    }
}
