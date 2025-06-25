export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  stock: number;
}

export interface Discount {
  type: "PERCENT" | "FIXED";
  value: number;
  appliedAt: string;
}

export interface ProductResponse {
  id: number;
  name: string;
  description: string;
  stock: number;
  isOutOfStock: boolean;
  price: number;
  finalPrice: number;
  discount: Discount | null;
  hasCouponApplied: boolean;
  createdAt: string;
  updatedAt: string;
  deletedAt: string | null;
}

export interface PagedResponse<T> {
  data: T[];
  meta: {
    page: number;
    limit: number;
    totalPages: number;
    totalItems: number;
  };
}

export interface ProductFilterParams {
  page?: number;
  limit?: number;
  search?: string;
  minPrice?: number | string;
  maxPrice?: number | string;
}
