import { redirect } from "react-router-dom";

fetch = ((originFetch) => {
  return async (...args) => {
    const url = args[0];
    console.log(`send request to ${url}`);
    let response = await originFetch(...args);

    if (!response.ok) {
      switch (response.status) {
        case 401:
          console.log("Not authenticated, try to refresh token");
          const refreshRes = await originFetch("/api/auth/refresh", {
            method: "POST",
          });
          if (!refreshRes.ok) {
            console.log("Cannot perform refresh, token maybe missing or bad");
            window.location.href = "/login";
            throw Error("Refresh token failed");
          }
          console.log("Got refresh token, retry the fetch");
          response = await originFetch(...args);
          if (!response.ok) {
            throw Error("Fetch failed after retry");
          }
          break;
        default:
          throw new Error("Fetch failed!");
      }
    }

    let data = await response.json();
    console.log(`response from ${response.url}: `, {
      ok: response.ok,
      status: response.status,
      data,
    });
    return data;
  };
})(fetch);

export async function get(endpoint) {
  const options = {
    method: "GET",
  };

  let data;
  try {
    data = await fetch(endpoint, options);
  } catch (e) {
    console.error(`Get failed: ${e.message}`);
    throw e;
  }
  return data;
}

export async function post(endpoint, body) {
  let data;
  try {
    data = await fetch(endpoint, {
      method: "POST",
      body: body ? JSON.stringify(body) : null,
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (e) {
    console.error(`Post failed: ${e.message}`);
    throw e;
  }
  return data;
}
