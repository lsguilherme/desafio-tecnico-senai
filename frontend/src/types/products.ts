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
