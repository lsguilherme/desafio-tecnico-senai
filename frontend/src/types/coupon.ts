export type CouponType = "PERCENT" | "FIXED";

export interface CouponResponse {
  id: number;
  code: string;
  type: CouponType;
  discountValue: number;
  oneShot: boolean;
  validFrom: string;
  validUntil: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: string | null;
}
