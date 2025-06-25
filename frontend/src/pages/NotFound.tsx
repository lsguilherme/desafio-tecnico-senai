import { Button } from '@/components/ui/button';
import { Frown } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function NotFound() {
    const navigate = useNavigate();

    const handleGoHome = () => {
        navigate('/');
    };

    return (
        <main className="flex-1 flex flex-col items-center justify-center p-6 bg-gray-100 text-gray-800">
            <Frown className="h-24 w-24 text-gray-400 mb-6" />
            <h1 className="text-5xl font-bold mb-4">404</h1>
            <p className="text-xl text-center mb-8">
                Ops! A página que você está procurando não foi encontrada.
            </p>
            <Button onClick={handleGoHome}>
                Voltar para a Página Inicial
            </Button>
        </main>
    );
};
