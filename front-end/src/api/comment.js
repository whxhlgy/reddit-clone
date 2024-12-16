import { get, post } from "@/api/requests";

export function findAllByPostId(postId) {
  return get(`/api/comments/${postId}`);
}

export function createComment(postId, body) {
  return post(`/api/comments/${postId}`, body);
}
