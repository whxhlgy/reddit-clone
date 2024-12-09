export async function get(endpoint) {
  console.debug(`request to ${endpoint}`);
  const options = {
    method: "GET",
  };

  const accessToken = localStorage.getItem("access_token");
  if (accessToken) {
    options.headers = options.headers || {};
    options.headers["Authorization"] = `Bearer ${accessToken}`;
  }

  const response = await fetch(endpoint, options);
  if (!response.ok) {
    throw new Error("Get failed");
  }
  return response.json();
}

export async function post(endpoint, data) {
  console.debug(`Post to ${endpoint}, data: ${JSON.stringify(data)}`);
  const response = await fetch(endpoint, {
    method: "POST",
    body: JSON.stringify(data),
    headers: {
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const errorDetails = await response.json();
    throw errorDetails;
  }
  return response.json();
}
