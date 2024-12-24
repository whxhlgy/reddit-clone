package me.project.backend.service.IService;

import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.payload.request.LikeRequest;

public interface ILikeService {

   int getUserReactionByCommentId(String username, long commentId);

   int getUserReactionByPostId(String username, long postId);

   LikeDTO likeCommentById(long commentId, LikeRequest likeRequest);

   LikeDTO likePostById(long postId, LikeRequest likeRequest);

   long countLikeByCommentId(long commentId);

   long countLikeByPostId(long postId);
}
