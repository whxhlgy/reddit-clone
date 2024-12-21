package me.project.backend.service.IService;

import me.project.backend.payload.dto.LikeDTO;

public interface ILikeService {

   int getUserReactionByCommentId(long commentId);

   LikeDTO likeCommentByCommentId(long commentId, int reaction);

}
