import { Avatar, AvatarFallback } from "../ui/avatar";

export default function Header() {
    return (
        <header className="bg-white border-b border-gray-200 p-4 flex items-center justify-end">
            <div className="flex items-center px-16 gap-4">
                <Avatar className="h-8 w-8">
                    <AvatarFallback className="bg-[#E2E8F0]">AM</AvatarFallback>
                </Avatar>
                <span className="font-medium text-gray-700">Arthur Morgan</span>
            </div>
        </header>
    );
}

