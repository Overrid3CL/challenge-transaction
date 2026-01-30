import { Link } from "react-router-dom";
import { Menu } from "lucide-react";
import { Button } from "@/components/ui/button";

interface NavbarProps {
  onMenuClick: () => void;
}

const menuLinks = [
  { path: "/", label: "Inicio" },
  { path: "/transactions", label: "Transacciones" },
];

export function Navbar({ onMenuClick }: NavbarProps) {
  return (
    <nav className="h-16 border-b bg-background">
      <div className="container mx-auto flex h-full items-center justify-between px-4">
        {/* Logo */}
        <Link to="/" viewTransition className="text-xl font-bold text-foreground">
          Web Transaction
        </Link>

        {/* Menú Desktop - visible solo en md y superior */}
        <div className="hidden items-center gap-6 md:flex">
          {menuLinks.map((link) => (
            <Link key={link.path} to={link.path} viewTransition className="text-sm font-medium text-muted-foreground transition-colors hover:text-foreground">
              {link.label}
            </Link>
          ))}
        </div>

        {/* Botones Desktop - visible solo en md y superior */}
        <div className="hidden items-center gap-4 md:flex"></div>

        {/* Botón Menú Mobile - visible solo en mobile */}
        <Button variant="ghost" size="icon" onClick={onMenuClick} className="md:hidden" aria-label="Abrir menú">
          <Menu className="size-6" />
        </Button>
      </div>
    </nav>
  );
}
