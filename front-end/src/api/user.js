import { get } from "@/api/requests";

export function whoami() {
  return get("/api/users/whoami");
}
