import { get, post } from "@/api/requests";

export function getPosts() {
  return get("/api/posts");
}

export function getPostByCommunityName(name) {
  return get(`/api/posts/community/${name}`);
}

export function getPostById(postId) {
  return get(`/api/posts/${postId}`);
}

export function createPost(communityName, redditPost) {
  return post(`/api/posts/community/${communityName}`, redditPost);
}
