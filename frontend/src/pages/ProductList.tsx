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
import { useState } from "react";
import { Label } from "@/components/ui/label";

interface Product {
    id: string;
    name: string;
    description: string;
    category: string;
    originalPrice?: number;
    price: number;
    discount?: string;
    stock: number;
}

const products: Product[] = [
    {
        id: "1",
        name: "Smartphone XYZ",
        description: "Smartphone premium com...",
        category: "Eletrônicos",
        originalPrice: 1999.99,
        price: 1799.99,
        discount: "10%",
        stock: 50,
    },
    {
        id: "2",
        name: "Notebook ultra",
        description: "Notebook para uso...",
        category: "Eletrônicos",
        price: 4500.0,
        stock: 30,
    },
    {
        id: "3",
        name: "Camiseta casual",
        description: "Camisa polo masculina...",
        category: "Roupas",
        price: 69.9,
        stock: 100,
    },
    {
        id: "4",
        name: "Tênis esportivo",
        description: "Tênis esportivo academia...",
        category: "Roupas",
        originalPrice: 299.99,
        price: 254.0,
        discount: "15%",
        stock: 15,
    },
    {
        id: "5",
        name: "Cafeteira elétrica",
        description: "Cafeteira elétrica 200W...",
        category: "Eletrônicos",
        price: 189.9,
        stock: 20,
    },
];
export default function ProductList() {
    const navigate = useNavigate();
    const [minPrice, setMinPrice] = useState<number | string>("");
    const [maxPrice, setMaxPrice] = useState<number | string>("");
    const [searchTerm, setSearchTerm] = useState<string>("");

    const isFilterActive =
        minPrice !== "" || maxPrice !== "" || searchTerm !== "";

    const handleCreateProduct = () => {
        navigate("/products/new");
    };

    const handleEditProduct = (product: Product) => {
        navigate(`/products/edit/${product.id}`, { state: { product } });
    };

    const handleDeleteProduct = (id: string) => {
        console.log(`Deletar produto com ID: ${id}`);
    };

    const handleDiscountProduct = (id: string) => {
        console.log(`Vender produto com ID: ${id}`);
    };

    const handleFilter = () => {
        console.log("Aplicar filtro:", { minPrice, maxPrice, searchTerm });
    };

    const handleClearFilters = () => {
        setMinPrice("");
        setMaxPrice("");
        setSearchTerm("");
        console.log("Limpar filtros");
    };
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
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[150px]">Nome</TableHead>
                            <TableHead className="w-[250px]">Descrição</TableHead>
                            <TableHead className="w-[150px]">Categoria</TableHead>
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

                                <TableCell>{product.category}</TableCell>

                                <TableCell className="flex  gap-4  relative">
                                    <div className="flex flex-col items-end">
                                        {product.originalPrice && (
                                            <span className="text-sm text-gray-500 line-through">
                                                R$ {product.originalPrice.toFixed(2).replace(".", ",")}
                                            </span>
                                        )}
                                        <span className="font-semibold whitespace-nowrap">
                                            R$ {product.price.toFixed(2).replace(".", ",")}
                                        </span>
                                    </div>

                                    {product.discount && (
                                        <div className="w-12 h-8 flex items-center justify-center rounded-xl bg-[#DCFCE7] text-[#158046]">
                                            {product.discount}
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
                                        >
                                            <Pencil className="h-4 w-4" />
                                        </Button>

                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="h-8 w-8 text-gray-500 hover:text-green-600"
                                            onClick={() => handleDiscountProduct(product.id)}
                                        >
                                            <DollarSign className="h-4 w-4" />
                                        </Button>

                                        <Button
                                            variant="ghost"
                                            size="icon"
                                            className="h-8 w-8 text-gray-500 hover:text-red-600"
                                            onClick={() => handleDeleteProduct(product.id)}
                                        >
                                            <Trash2 className="h-4 w-4" />
                                        </Button>
                                    </div>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </main>
    );
}
