package me.project.backend.service.IService;

import me.project.backend.payload.dto.LikeDTO;

public interface ILikeService {

   int getUserReactionByCommentId(long commentId);

   int getUserReactionByPostId(long postId);

   LikeDTO likeCommentById(long commentId, int reaction);

   LikeDTO likePostById(long postId, int reaction);

   int countLikeByCommentId(long commentId);

   int countLikeByPostId(long postId);
}
