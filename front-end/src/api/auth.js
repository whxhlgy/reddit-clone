import { post } from "@/api/requests";

export function login(data) {
  return post("/api/auth/login", data);
}
