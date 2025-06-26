import { useCallback, useEffect, useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Lightbulb } from 'lucide-react';

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";

import { applyCouponToProduct, applyPercentDiscount } from '@/services/productService';
import { findAllCoupons } from '@/services/couponService';
import type { CouponResponse } from '@/types/coupon';

interface DiscountModalProps {
    isOpen: boolean;
    onClose: () => void;
    productId: number | null;
    onDiscountApplied: () => void;
}

export default function DiscountModal({ isOpen, onClose, productId, onDiscountApplied }: DiscountModalProps) {
    const [discountType, setDiscountType] = useState<'coupon' | 'percent'>('coupon');
    const [couponCode, setCouponCode] = useState<string>('');
    const [percentValue, setPercentValue] = useState<number | string>('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [availableCoupons, setAvailableCoupons] = useState<CouponResponse[]>([]);
    const [fetchingCoupons, setFetchingCoupons] = useState(false);
    const fetchAvailableCoupons = useCallback(async () => {
        setFetchingCoupons(true);
        try {
            const coupons = await findAllCoupons();
            setAvailableCoupons(coupons);
        } catch (err: unknown) {
            console.error("Erro ao carregar cupons:", err);
            setError("Não foi possível carregar os cupons disponíveis.");
        } finally {
            setFetchingCoupons(false);
        }
    }, []);

    useEffect(() => {
        if (isOpen) {
            fetchAvailableCoupons();
        }
    }, [isOpen, fetchAvailableCoupons]);

    const handleClose = () => {
        setDiscountType('coupon');
        setCouponCode('');
        setPercentValue('');
        setLoading(false);
        setError(null);
        onClose();
    };

    const handleApplyDiscount = async () => {
        if (!productId) {
            setError('ID do produto não fornecido.');
            return;
        }

        setLoading(true);
        setError(null);

        try {
            if (discountType === 'coupon') {
                if (!couponCode.trim()) {
                    setError('O código do cupom não pode estar vazio.');
                    setLoading(false);
                    return;
                }
                await applyCouponToProduct(productId, couponCode);
            } else {
                const percent = parseFloat(percentValue as string);
                if (isNaN(percent) || percent < 1 || percent > 100) {
                    setError('O percentual deve ser um número entre 1 e 100.');
                    setLoading(false);
                    return;
                }
                await applyPercentDiscount(productId, percent);
            }
            onDiscountApplied();
            handleClose();
        } catch (err: unknown) {
            console.error("Erro ao aplicar desconto:", err);
            if (err instanceof Error) {
                setError(`Erro: ${err.message}`);
            } else {
                setError('Ocorreu um erro desconhecido ao aplicar o desconto.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={handleClose}>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle className="flex items-center">
                        <Lightbulb className="mr-2 h-6 w-6 text-yellow-500" />
                        Aplicar Desconto
                    </DialogTitle>
                    <DialogDescription>
                        Escolha como aplicar o desconto ao produto
                    </DialogDescription>
                </DialogHeader>

                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                        <strong className="font-bold">Erro:</strong>
                        <span className="block sm:inline"> {error}</span>
                    </div>
                )}

                <div className="flex gap-2 my-4">
                    <Button
                        variant={discountType === 'coupon' ? 'default' : 'outline'}
                        onClick={() => setDiscountType('coupon')}
                        disabled={loading}
                        className="flex-1"
                    >
                        Código Cupom
                    </Button>
                    <Button
                        variant={discountType === 'percent' ? 'default' : 'outline'}
                        onClick={() => setDiscountType('percent')}
                        disabled={loading}
                        className="flex-1"
                    >
                        Percentual Direto
                    </Button>
                </div>

                {discountType === 'coupon' ? (
                    <div className="space-y-4">
                        <div>
                            <Label htmlFor="coupon-code">Código do Cupom</Label>
                            <Input
                                id="coupon-code"
                                placeholder="Digite o código do cupom"
                                value={couponCode}
                                onChange={(e) => setCouponCode(e.target.value)}
                                disabled={loading}
                            />
                        </div>
                        <div className="text-sm text-gray-500 mt-2">
                            Cupons disponíveis para teste:
                            {fetchingCoupons ? (
                                <p>Carregando cupons...</p>
                            ) : availableCoupons.length > 0 ? (
                                <div className="flex flex-wrap gap-2 mt-2">
                                    {availableCoupons.map(coupon => (
                                        <Button
                                            key={coupon.id}
                                            size="sm"
                                            variant="secondary"
                                            onClick={() => setCouponCode(coupon.code)}
                                            disabled={loading}
                                        >
                                            {coupon.code} ({coupon.type === 'PERCENT' ? `${coupon.discountValue}%` : `R$ ${coupon.discountValue}`})
                                        </Button>
                                    ))}
                                </div>
                            ) : (
                                <p>Nenhum cupom disponível.</p>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="space-y-4">
                        <div>
                            <Label htmlFor="percent-value">Percentual de desconto</Label>
                            <Input
                                id="percent-value"
                                type="number"
                                placeholder="Ex: 10%"
                                value={percentValue}
                                onChange={(e) => setPercentValue(e.target.value)}
                                min="1"
                                max="100"
                                step="1"
                                disabled={loading}
                            />
                            <p className="text-xs text-gray-500 mt-1">Digite um valor entre 1% e 100%</p>
                        </div>
                    </div>
                )}

                <DialogFooter className="mt-6">
                    <Button variant="outline" onClick={handleClose} disabled={loading}>
                        Cancelar
                    </Button>
                    <Button onClick={handleApplyDiscount} disabled={loading}>
                        {loading ? 'Aplicando...' : 'Aplicar'}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}