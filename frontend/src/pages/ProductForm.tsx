import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectGroup, SelectItem, SelectLabel, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { Textarea } from "@/components/ui/textarea";
import { Package } from "lucide-react";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";

interface Product {
    id: string;
    name: string;
    description: string;
    category: string;
    price: number;
    stock: number;
    originalPrice?: number;
    discount?: string;
    isActive?: boolean; // Para o switch "Produto Ativo"
}

export default function ProductForm() {
    const navigate = useNavigate();
    const { id } = useParams<{ id?: string }>();
    const location = useLocation();

    const isEditing = !!id;

    const [productForm, setProductForm] = useState<Partial<Product>>({
        name: '',
        description: '',
        category: '',
        price: 0,
        stock: 0,
        isActive: true,
    });

    useEffect(() => {
        if (isEditing && location.state && (location.state as { product: Product }).product) {
            const productToEdit = (location.state as { product: Product }).product;
            setProductForm(productToEdit);
        } else if (isEditing) {
            const fetchedProduct: Product = {
                id: id || '',
                name: 'Smartphone XYZ',
                description: 'Smartphone premium com 64GB',
                category: 'Eletrônicos',
                originalPrice: 1999.99,
                price: 1799.99,
                stock: 50,
                isActive: true,
            };
            setProductForm(fetchedProduct);
        }
    }, [id, isEditing, location.state]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { id, value } = e.target;
        setProductForm((prev) => ({
            ...prev,
            [id]: id === 'price' || id === 'stock' ? parseFloat(value) : value,
        }));
    };

    const handleSelectChange = (value: string) => {
        setProductForm((prev) => ({ ...prev, category: value }));
    };

    const handleSwitchChange = (checked: boolean) => {
        setProductForm((prev) => ({ ...prev, isActive: checked }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (isEditing) {
            console.log('Salvar alterações do produto:', productForm);
        } else {
            console.log('Cadastrar novo produto:', productForm);
        }
        navigate('/products');
    };

    const handleCancel = () => {
        navigate('/products');
    };

    return (
        <main className="p-6 flex-1">
            <div className="flex items-center mb-6">
                <h1 className="text-3xl font-semibold text-gray-800 flex items-center">
                    <Package className="mr-3 h-7 w-7 text-gray-600" />
                    {isEditing ? 'Editar Produto' : 'Cadastro de Produto'}
                </h1>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                <div className="flex items-center text-sm text-gray-600 mb-6 pb-4 border-b border-gray-200">
                    <span className="font-semibold text-red-500 mr-2">!</span>
                    Dados do produto: O campo abaixo é obrigatório para o cadastro.
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <Label htmlFor="name">Nome do produto <span className="text-red-500">*</span></Label>
                            <Input id="name" value={productForm.name || ''} onChange={handleChange} placeholder="Informe o nome do produto" required />
                        </div>
                        <div>
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
                        </div>
                    </div>

                    <div>
                        <Label htmlFor="description">Descrição <span className="text-red-500">*</span></Label>
                        <Textarea id="description" value={productForm.description || ''} onChange={handleChange} placeholder="Descrição detalhada do produto" required />
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
            </div>
        </main>
    );
};