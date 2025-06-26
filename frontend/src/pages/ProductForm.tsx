import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
// import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { Textarea } from "@/components/ui/textarea";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";

import { createProduct, deleteProduct, getProductById, restoreProduct, updateProduct } from "@/services/productService";

import { type ProductRequest, type ProductResponse } from "@/types/products";
import type { JsonPatchOperation } from "@/types/json-patch";

export default function ProductForm() {
    const navigate = useNavigate();
    const { id } = useParams<{ id?: string }>();
    const location = useLocation();

    const isEditing = !!id;

    const [productForm, setProductForm] = useState<Partial<ProductRequest & { isActive: boolean }>>({
        name: '',
        description: '',
        price: 0,
        stock: 0,
        isActive: true,
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadProduct = async (productId: string) => {
            setLoading(true);
            setError(null);
            try {
                const productFromState = (location.state as { product?: ProductResponse })?.product;

                if (productFromState) {
                    setProductForm({
                        name: productFromState.name,
                        description: productFromState.description,
                        price: productFromState.price,
                        stock: productFromState.stock,
                        isActive: productFromState.deletedAt === null,
                    });
                    setLoading(false);
                } else {
                    const data = await getProductById(productId);
                    setProductForm({
                        name: data.name,
                        description: data.description,
                        price: data.price,
                        stock: data.stock,
                        isActive: data.deletedAt === null,
                    });
                    setLoading(false);
                }
            } catch (err: unknown) {
                console.log(err);
                // setError(err.message);
                setLoading(false);
            }
        };

        if (isEditing) {
            loadProduct(id);
        }
    }, [id, isEditing, location.state]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { id, value } = e.target;
        setProductForm((prev) => ({
            ...prev,
            [id]: id === 'price' || id === 'stock' ? parseFloat(value) : value,
        }));
    };

    /* const handleSelectChange = (value: string) => {
        setProductForm((prev) => ({ ...prev, category: value }));
    }; */

    const handleSwitchChange = async (checked: boolean) => {
        setProductForm((prev) => ({ ...prev, isActive: checked }));

        if (isEditing && id) {
            setLoading(true);
            setError(null);
            try {
                if (checked) {
                    await restoreProduct(id);
                } else {
                    await deleteProduct(parseInt(id));
                    navigate("/products");
                }
            } catch (err: unknown) {
                if (err instanceof Error) {
                    setError(`Erro ao atualizar status: ${err.message}`);
                } else {
                    setError('Ocorreu um erro desconhecido ao atualizar o status do produto.');
                }
                setProductForm((prev) => ({ ...prev, isActive: !checked }));
            } finally {
                setLoading(false);
            }
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const payload: ProductRequest = {
            name: productForm.name || '',
            description: productForm.description || '',
            price: productForm.price !== undefined ? productForm.price : 0,
            stock: productForm.stock !== undefined ? productForm.stock : 0,
        };

        try {
            if (isEditing && id) {
                const patchOperations: JsonPatchOperation[] = [];
                if (location.state && (location.state as { product: ProductResponse }).product) {
                    const originalProduct = (location.state as { product: ProductResponse }).product;
                    if (payload.name !== originalProduct.name) patchOperations.push({ op: "replace", path: "/name", value: payload.name });
                    if (payload.description !== originalProduct.description) patchOperations.push({ op: "replace", path: "/description", value: payload.description });
                    if (payload.price !== originalProduct.price) patchOperations.push({ op: "replace", path: "/price", value: payload.price });
                    if (payload.stock !== originalProduct.stock) patchOperations.push({ op: "replace", path: "/stock", value: payload.stock });
                } else {
                    patchOperations.push({ op: "replace", path: "/name", value: payload.name });
                    patchOperations.push({ op: "replace", path: "/description", value: payload.description });
                    patchOperations.push({ op: "replace", path: "/price", value: payload.price });
                    patchOperations.push({ op: "replace", path: "/stock", value: payload.stock });
                }
                await updateProduct(id, patchOperations);
            } else {
                await createProduct(payload);
            }
            navigate('/products');
        } catch (err: unknown) {
            console.log(err);
            //setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        navigate('/products');
    };

    if (loading && isEditing) {
        return (
            <main className="p-6 flex-1 flex items-center justify-center">
                <p>Loading product data...</p>
            </main>
        );
    }

    return (
        <main className="px-16 py-12">
            <div className="flex items-center mb-6">
                <h1 className="text-3xl font-semibold text-gray-800 flex gap-4 items-center">
                    {isEditing ? <img src="/src/assets/icons/edit-produto.svg" /> : <img src="/src/assets/icons/cadastro-produto.svg" />}
                    {isEditing ? 'Editar Produto' : 'Cadastro de Produto'}
                </h1>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                <div className="flex items-center text-sm text-gray-600 mb-6 pb-4 border-b border-gray-200">
                    Dados do produto: O campo abaixo é obrigatório para o cadastro.
                </div>

                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                        <strong className="font-bold">Error:</strong>
                        <span className="block sm:inline"> {error}</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <Label htmlFor="name">Nome do produto <span className="text-red-500">*</span></Label>
                            <Input id="name" value={productForm.name || ''} onChange={handleChange} placeholder="Informe o nome do produto" required />
                        </div>
                        {/* <div>
                            <Label htmlFor="category">Categoria <span className="text-red-500">*</span></Label>
                            <Select onValueChange={handleSelectChange} value={productForm.category || ''} required>
                                <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Categoria do produto" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectGroup>
                                        <SelectLabel>Categorias</SelectLabel>
                                        <SelectItem value="Eletrônicos">Eletrônicos</SelectItem>
                                        <SelectItem value="Roupas">Roupas</SelectItem>
                                        <SelectItem value="Alimentos">Alimentos</SelectItem>
                                        <SelectItem value="Livros">Livros</SelectItem>
                                    </SelectGroup>
                                </SelectContent>
                            </Select>
                        </div> */}
                    </div>

                    <div>
                        <Label htmlFor="description">Descrição</Label>
                        <Textarea id="description" value={productForm.description || ''} onChange={handleChange} placeholder="Descrição detalhada do produto" />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <Label htmlFor="price">Preço <span className="text-red-500">*</span></Label>
                            <Input
                                id="price"
                                type="number"
                                value={productForm.price !== undefined ? productForm.price : ''}
                                onChange={handleChange}
                                placeholder="R$ 0,00"
                                step="0.01"
                                required
                            />
                        </div>
                        <div>
                            <Label htmlFor="stock">Estoque <span className="text-red-500">*</span></Label>
                            <Input
                                id="stock"
                                type="number"
                                value={productForm.stock !== undefined ? productForm.stock : ''}
                                onChange={handleChange}
                                placeholder="0"
                                required
                            />
                        </div>
                    </div>

                    {isEditing && (
                        <div className="flex items-center space-x-2 pt-4">
                            <Switch
                                id="product-active"
                                checked={productForm.isActive}
                                onCheckedChange={handleSwitchChange}
                            />
                            <Label htmlFor="product-active">Produto Ativo</Label>
                        </div>
                    )}

                    <div className="flex justify-end space-x-4 pt-6 border-t border-gray-200 mt-6">
                        <Button type="button" variant="outline" onClick={handleCancel}>
                            Cancelar
                        </Button>
                        <Button type="submit">
                            {isEditing ? 'Salvar alterações' : 'Cadastrar'}
                        </Button>
                    </div>
                </form>
            </div >
        </main >
    );
};