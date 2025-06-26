import { handleResponse } from "@/lib/api";
import { type CouponResponse } from "@/types/coupon";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function findAllCoupons(): Promise<CouponResponse[]> {
  const response = await fetch(`${API_BASE_URL}/coupons`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
  return handleResponse(response);
}
