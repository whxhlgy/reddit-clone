import { get, post } from "@/api/requests";

export function getPosts() {
  return get("/api/posts");
}

export function getPostByCommunityName(name, params) {
  return get(`/api/posts/community/${name}`, params);
}

export function getPostById(postId) {
  return get(`/api/posts/${postId}`);
}

export function createPost(communityName, redditPost) {
  return post(`/api/posts/community/${communityName}`, redditPost);
}
