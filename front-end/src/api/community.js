import { get, post } from "@/api/requests";

export function getAllCommunities() {
  return get("/api/communities");
}

export async function createCommunity(community) {
  return post("/api/communities", community);
}

export function getCommunityByName(name) {
  return get(`/api/communities/name/${name}`);
}

export function subscribeCommunityByName(name) {
  return post(`/api/sub/${name}`);
}

export function getCommunityByUsername(username) {
  return get(`/api/users/name/${username}/sub`);
}
