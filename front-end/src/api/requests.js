export async function get(endpoint) {
  const response = await fetch(endpoint);
  if (!response.ok) {
    throw new Response("Get failed", {
      status: response.status,
    });
  }
  return response;
}

export async function post(endpoint, data) {
  const response = await fetch(endpoint, {
    method: "POST",
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    throw new Response("Post failed", {
      status: response.status,
    });
  }
  return response;
}
