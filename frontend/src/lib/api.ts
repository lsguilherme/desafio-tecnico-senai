export async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || JSON.stringify(errorData));
  }

  const contentType = response.headers.get("content-type");
  const contentLength = response.headers.get("content-length");

  if (
    response.status === 204 ||
    (contentLength !== null && parseInt(contentLength) === 0)
  ) {
    return {} as T;
  }

  if (!contentType || !contentType.includes("application/json")) {
    console.warn(
      `[handleResponse] Resposta com sucesso (status ${
        response.status
      }) mas Content-Type não é JSON: ${contentType || "nenhum"}.`
    );
    return {} as T;
  }

  return response.json();
}
