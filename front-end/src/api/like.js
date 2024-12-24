import { post } from "@/api/requests";

export function likeComment(commentId, body) {
  return post(`/api/like/comment/${commentId}`, body);
}

export function likePost(postId, body) {
  return post(`/api/like/post/${postId}`, body);
}
