package me.project.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Post;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.util.RedisKeys;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostCacheService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public PostCacheService(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public long increaseViewCount(long postId, String username) {
        String key = RedisKeys.getPostViewKey(postId);
        stringRedisTemplate.opsForHyperLogLog().add(key, username);
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }

    public long getViewCount(long postId) {
        String key = RedisKeys.getPostViewKey(postId);
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }

    public void savePost(Post post) {
        log.debug("Saving a post to redis");
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setUsername(post.getUser().getUsername());
        String postString;
        try {
            postString = objectMapper.writeValueAsString(postDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.debug("Saved: {}", postString);
        stringRedisTemplate.opsForValue().set(RedisKeys.getPostKey(post.getId()), postString);
    }

    public PostDTO getPost(long postId) {
        log.debug("Getting a post from redis");
        String key = RedisKeys.getPostKey(postId);
        String postString = stringRedisTemplate.opsForValue().get(key);
        if (postString == null) {
            log.debug("No post found");
            return null;
        }
        log.debug("got a post json string from redis: {}", postString);
        try {
            return objectMapper.readValue(postString, PostDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
