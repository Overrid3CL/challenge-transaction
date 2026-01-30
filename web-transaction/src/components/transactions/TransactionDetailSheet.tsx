"use client";

import type { TransactionResponseDTO } from "@/types/transaction";
import { formatCurrency, formatDateTime } from "@/lib/utils/format";
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetFooter } from "@/components/ui/sheet";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";

export interface TransactionDetailSheetProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  transaction: TransactionResponseDTO | null;
  onEdit: (transaction: TransactionResponseDTO) => void;
  onDelete: (id: number) => void;
}

export function TransactionDetailSheet({ open, onOpenChange, transaction, onEdit, onDelete }: TransactionDetailSheetProps) {
  if (!transaction) return null;

  const handleEdit = () => {
    onOpenChange(false);
    onEdit(transaction);
  };

  const handleDelete = () => {
    onOpenChange(false);
    onDelete(transaction.id);
  };

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent side="right" className="flex flex-col">
        <SheetHeader>
          <SheetTitle>Detalle de transacción</SheetTitle>
        </SheetHeader>
        <div className="flex flex-1 flex-col gap-4 p-4">
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">ID</p>
            <p className="font-medium">{transaction.id}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">Usuario</p>
            <p className="font-medium">{transaction.userName}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">Comercio</p>
            <p className="font-medium">{transaction.businessName}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">Monto</p>
            <p className="font-medium">{formatCurrency(transaction.amount)}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">Fecha</p>
            <p className="font-medium">{formatDateTime(transaction.transactionDate)}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-muted-foreground">Descripción</p>
            <p className="font-medium">{transaction.description ?? <span className="text-muted-foreground">-</span>}</p>
          </div>
        </div>
        <SheetFooter className="flex-row gap-2 sm:flex-row">
          <Button variant="outline" onClick={handleEdit} className="flex-1">
            <Pencil className="mr-2 h-4 w-4" />
            Editar
          </Button>
          <Button variant="destructive" onClick={handleDelete} className="flex-1">
            <Trash2 className="mr-2 h-4 w-4" />
            Eliminar
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
