import { Link } from "react-router-dom";
import { Sheet, SheetContent, SheetHeader, SheetTitle } from "@/components/ui/sheet";

interface MobileSidebarProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const menuLinks = [
  { path: "/", label: "Inicio" },
  { path: "/transactions", label: "Transacciones" },
  { path: "/users", label: "Usuarios" },
];

export function MobileSidebar({ open, onOpenChange }: MobileSidebarProps) {
  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent side="left" className="flex w-[280px] flex-col sm:w-[300px]">
        <SheetHeader>
          <SheetTitle className="text-left text-xl font-bold">Web Transaction</SheetTitle>
        </SheetHeader>

        <nav className="mt-8 flex flex-col gap-2">
          {menuLinks.map((link) => (
            <Link key={link.path} to={link.path} viewTransition onClick={() => onOpenChange(false)} className="rounded-md px-4 py-3 text-sm font-medium text-muted-foreground transition-colors hover:bg-accent hover:text-accent-foreground">
              {link.label}
            </Link>
          ))}
        </nav>
      </SheetContent>
    </Sheet>
  );
}
