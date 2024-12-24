package me.project.backend.service.IService;

import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.payload.request.LikeRequest;

public interface ILikeService {

   int getUserReactionByCommentId(long commentId);

   int getUserReactionByPostId(long postId);

   LikeDTO likeCommentById(long commentId, LikeRequest likeRequest);

   LikeDTO likePostById(long postId, LikeRequest likeRequest);

   int countLikeByCommentId(long commentId);

   int countLikeByPostId(long postId);
}
