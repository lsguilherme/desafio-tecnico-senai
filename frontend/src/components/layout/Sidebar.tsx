import { LogOut } from 'lucide-react';

export default function Sidebar() {
    return (
        <aside className="w-64 bg-white min-h-screen p-4 flex flex-col justify-between">
            <div>
                <div className="flex items-center mb-10 mt-2 px-3">
                    <img src="/src/assets/logo.svg" alt="Grupo A Logo" className="h-8 mr-2" />
                </div>
                <nav>
                    <ul className="space-y-2">
                        <li>
                            <a href="#" className="flex gap-2 items-center p-3 rounded-md text-custom-gray hover:bg-hover-gray transition-colors duration-200">
                                <img src="/src/assets/icons/dashboard.svg" />
                                <span>Dashboard</span>
                            </a>
                        </li>
                        <li>
                            <a href="#" className="flex gap-2 items-center p-3 rounded-md bg-hover-gray text-custom-gray font-medium">
                                <img src="/src/assets/icons/produtos.svg" />
                                <span>Produtos</span>
                            </a>
                        </li>
                        <li>
                            <a href="#" className="flex gap-2 items-center p-3 rounded-md text-custom-gray hover:bg-hover-gray transition-colors duration-200">
                                <img src="/src/assets/icons/relatorios.svg" />
                                <span>Relatórios</span>
                            </a>
                        </li>
                        <li>
                            <a href="#" className="flex gap-2 items-center p-3 rounded-md text-custom-gray hover:bg-hover-gray transition-colors duration-200">
                                <img src="/src/assets/icons/administracao.svg" />
                                <span>Administração</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            <div className="px-3">
                <a href="#" className="flex items-center p-3 rounded-md text-[#F87171] hover:bg-hover-gray transition-colors duration-200">
                    <LogOut className="mr-3 h-5 w-5" />
                    Sair
                </a>
            </div>
        </aside>
    );
}