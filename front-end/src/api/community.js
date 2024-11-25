import { get, post } from "@/api/requests";

export function getAllCommunities() {
  return get("/api/communities");
}

export async function createCommunity(community) {
  return post("/api/communities", community);
}
