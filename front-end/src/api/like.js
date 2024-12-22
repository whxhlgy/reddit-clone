import { post } from "@/api/requests";

export function likeComment(commentId, reaction) {
  return post(`/api/like/comment/${commentId}`, null, { reaction });
}
