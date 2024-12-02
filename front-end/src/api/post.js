import { get, post } from "@/api/requests";

export function getPosts() {
  return get("/api/posts");
}

export function createPost(redditPost) {
  return post("/api/posts", redditPost);
}
