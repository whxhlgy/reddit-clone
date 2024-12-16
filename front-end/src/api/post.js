import { get, post } from "@/api/requests";

export function getPosts() {
  return get("/api/posts");
}

export function getPostById(postId) {
  return get(`/api/posts/${postId}`);
}

export function createPost(redditPost) {
  return post("/api/posts", redditPost);
}
