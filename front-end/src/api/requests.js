export async function get(endpoint) {
  const response = await fetch(endpoint);
  return response;
}

export async function post(endpoint, data) {
  console.log(`POST ${endpoint}, data:`, JSON.stringify(data));
  const response = await fetch(endpoint, {
    method: "POST",
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
    },
  });
  return response;
}
