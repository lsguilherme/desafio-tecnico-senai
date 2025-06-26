import { DollarSign, Pencil, Plus, Trash2 } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { useNavigate } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { Label } from "@/components/ui/label";
import type { ProductResponse } from "@/types/products";
import { getProducts } from "@/services/productService";

export default function ProductList() {
    const navigate = useNavigate();
    const [products, setProducts] = useState<ProductResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [minPrice, setMinPrice] = useState<number | string>("");
    const [maxPrice, setMaxPrice] = useState<number | string>("");
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const limit = 10;

    const isFilterActive =
        minPrice !== "" || maxPrice !== "" || searchTerm !== "";

    const fetchProducts = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await getProducts({
                page: page,
                limit: limit,
                search: searchTerm,
                minPrice: minPrice,
                maxPrice: maxPrice,
            });
            setProducts(data.data);
            setTotalPages(data.meta.totalPages);
        } catch (err: unknown) {
            console.error("Erro ao carregar produtos:", err);
            if (err instanceof Error) {
                setError(`Erro ao carregar produtos: ${err.message}`);
            } else {
                setError('Ocorreu um erro desconhecido ao carregar os produtos.');
            }
        } finally {
            setLoading(false);
        }
    }, [page, limit, searchTerm, minPrice, maxPrice]);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    const handleCreateProduct = () => {
        navigate("/products/new");
    };

    const handleEditProduct = (product: ProductResponse) => {
        navigate(`/products/edit/${product.id}`, { state: { product } });
    };

    /* const handleDeleteProduct = (id: string) => {
        console.log(`Deletar produto com ID: ${id}`);
    };

    const handleDiscountProduct = (id: string) => {
        console.log(`Vender produto com ID: ${id}`);
    }; */

    const handleFilter = () => {
        setPage(0);
        fetchProducts();
    };

    const handleClearFilters = () => {
        setMinPrice("");
        setMaxPrice("");
        setSearchTerm("");
        setPage(0);
        fetchProducts();
    };

    if (loading && products.length === 0 && !isFilterActive) {
        return (
            <main className="px-16 py-12 flex-1 flex items-center justify-center">
                <p>Carregando produtos...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main className="px-16 py-12 flex-1 flex items-center justify-center">
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                    <strong className="font-bold">Erro:</strong>
                    <span className="block sm:inline"> {error}</span>
                </div>
            </main>
        );
    }

    return (
        <main className="px-16 py-12 flex-1">
            <div className="flex items-center mb-6">
                <h1 className="text-3xl font-semibold flex gap-4 items-center">
                    <img src="/src/assets/icons/produtos.svg" className="size-10" />
                    <span className="text-black">Produtos</span>
                </h1>
            </div>

            <div className="mb-6">
                <div className="flex flex-wrap items-end gap-x-3 gap-y-4">
                    <div className="flex-1 min-w-[120px] max-w-[200px]">
                        <Label htmlFor="min-price">Preço mínimo</Label>
                        <Input
                            id="min-price"
                            type="number"
                            placeholder="R$ 0,00"
                            value={minPrice}
                            onChange={(e) => setMinPrice(e.target.value)}
                            className="bg-white py-6"
                        />
                    </div>

                    <div className="flex-1 min-w-[120px] max-w-[200px]">
                        <Label htmlFor="max-price">Preço máximo</Label>
                        <Input
                            id="max-price"
                            type="number"
                            placeholder="R$ 999,99"
                            value={maxPrice}
                            onChange={(e) => setMaxPrice(e.target.value)}
                            className="bg-white py-6"
                        />
                    </div>

                    <div className="w-auto">
                        {isFilterActive ? (
                            <Button
                                variant="outline"
                                className="py-6"
                                onClick={handleClearFilters}
                            >
                                Limpar Filtro
                            </Button>
                        ) : (
                            <Button onClick={handleFilter} className="py-6">
                                Filtrar
                            </Button>
                        )}
                    </div>

                    <div className="flex flex-1 items-center gap-x-2 min-w-[250px]">
                        <Input
                            placeholder="Buscar produto..."
                            className="bg-white py-6"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="w-auto ml-auto">
                        <Button onClick={handleCreateProduct} className="py-6 px-12">
                            <Plus className="mr-1 h-6 w-6" /> Criar Produto
                        </Button>
                    </div>
                </div>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                {products.length === 0 && !loading && !error && isFilterActive ? (
                    <p className="text-center text-gray-500">Nenhum produto encontrado com os filtros aplicados.</p>
                ) : products.length === 0 && !loading && !error && !isFilterActive ? (
                    <p className="text-center text-gray-500">Nenhum produto cadastrado ainda.</p>
                ) : (
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[150px]">Nome</TableHead>
                                <TableHead className="w-[250px]">Descrição</TableHead>
                                <TableHead className="w-[150px]">Preço</TableHead>
                                <TableHead className="text-center w-[100px]">Estoque</TableHead>
                                <TableHead className="text-right w-[100px]">Ações</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {products.map((product) => (
                                <TableRow key={product.id}>
                                    <TableCell className="font-medium">{product.name}</TableCell>
                                    <TableCell className="text-gray-600 truncate max-w-[250px]">
                                        {product.description}
                                    </TableCell>
                                    <TableCell className="flex gap-4 relative">
                                        <div className="flex flex-col items-end">
                                            {product.hasCouponApplied && product.price !== product.finalPrice && (
                                                <span className="text-sm text-gray-500 line-through">
                                                    R$ {product.price.toFixed(2).replace(".", ",")}
                                                </span>
                                            )}
                                            <span className="font-semibold whitespace-nowrap">
                                                R$ {product.finalPrice.toFixed(2).replace(".", ",")}
                                            </span>
                                        </div>
                                        {product.discount && (
                                            <div className="w-auto px-3 flex items-center justify-center rounded-lg bg-[#DCFCE7] text-[#158046]">
                                                {product.discount.type === 'PERCENT' ? `${product.discount.value}%` : `R$ ${product.discount.value.toFixed(2).replace(".", ",")}`}
                                            </div>
                                        )}
                                    </TableCell>
                                    <TableCell className="text-center">{product.stock}</TableCell>
                                    <TableCell>
                                        <div className="flex justify-end">
                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                className="h-8 w-8 text-gray-500 hover:text-blue-600"
                                                onClick={() => handleEditProduct(product)}
                                                disabled={loading}
                                            >
                                                <Pencil className="h-4 w-4" />
                                            </Button>

                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                className="h-8 w-8 text-gray-500 hover:text-green-600"
                                                // onClick={() => handleDiscountProduct()}
                                                disabled={loading}
                                            >
                                                <DollarSign className="h-4 w-4" />
                                            </Button>

                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                className="h-8 w-8 text-gray-500 hover:text-red-600"
                                                // onClick={() => handleDeleteProduct()}
                                                disabled={loading}
                                            >
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </div>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                )}
            </div>
            {totalPages > 1 && (
                <div className="flex justify-center mt-4 space-x-2">
                    <Button onClick={() => setPage(prev => Math.max(0, prev - 1))} disabled={page === 0 || loading}>
                        Anterior
                    </Button>
                    <span className="self-center">Página {page + 1} de {totalPages}</span>
                    <Button onClick={() => setPage(prev => Math.min(totalPages - 1, prev + 1))} disabled={page === totalPages - 1 || loading}>
                        Próxima
                    </Button>
                </div>
            )}
        </main>
    );
}
