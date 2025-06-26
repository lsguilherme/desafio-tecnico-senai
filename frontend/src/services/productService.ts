import type { JsonPatchOperation } from "@/types/json-patch";
import {
  type PagedResponse,
  type ProductFilterParams,
  type ProductRequest,
  type ProductResponse,
} from "@/types/products";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(
      errorData.message || `API request failed with status ${response.status}`
    );
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

export async function createProduct(
  productData: ProductRequest
): Promise<ProductResponse> {
  const response = await fetch(`${API_BASE_URL}/products`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(productData),
  });
  return handleResponse(response);
}

export async function updateProduct(
  id: string,
  patchOperations: JsonPatchOperation[]
): Promise<ProductResponse> {
  const response = await fetch(`${API_BASE_URL}/products/${id}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json-patch+json",
    },
    body: JSON.stringify(patchOperations),
  });
  return handleResponse(response);
}

export async function getProductById(id: string): Promise<ProductResponse> {
  const response = await fetch(`${API_BASE_URL}/products/${id}`);
  return handleResponse(response);
}

export async function deleteProduct(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/products/${id}`, {
    method: "DELETE",
  });

  if (response.status === 204) {
    return;
  }

  await handleResponse(response);
}

export async function getProducts(
  params: ProductFilterParams = {}
): Promise<PagedResponse<ProductResponse>> {
  const queryParams = new URLSearchParams();
  if (params.page !== undefined)
    queryParams.append("page", params.page.toString());
  if (params.limit !== undefined)
    queryParams.append("limit", params.limit.toString());
  if (params.search) queryParams.append("search", params.search);
  if (params.minPrice)
    queryParams.append("minPrice", params.minPrice.toString());
  if (params.maxPrice)
    queryParams.append("maxPrice", params.maxPrice.toString());

  const response = await fetch(
    `${API_BASE_URL}/products?${queryParams.toString()}`
  );
  return handleResponse(response);
}

export async function restoreProduct(id: string): Promise<ProductResponse> {
  const response = await fetch(`${API_BASE_URL}/products/${id}/restore`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  });
  return handleResponse(response);
}
