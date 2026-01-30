"use client";

import type { ColumnDef } from "@tanstack/react-table";
import type { TransactionResponseDTO } from "@/types/transaction";
import { formatCurrency, formatDateTime } from "@/lib/utils/format";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";

export interface TransactionColumnsOptions {
  onEdit: (transaction: TransactionResponseDTO) => void;
  onDelete: (id: number) => void;
}

/**
 * Devuelve las definiciones de columnas para la tabla de transacciones.
 * En mobile (meta.isMobile) la columna userName muestra también businessName debajo (Opción B).
 */
export function getTransactionColumns({ onEdit, onDelete }: TransactionColumnsOptions): ColumnDef<TransactionResponseDTO>[] {
  return [
    {
      accessorKey: "id",
      header: "ID",
      cell: ({ row }) => <span className="font-medium">{row.original.id}</span>,
    },
    {
      accessorKey: "userName",
      header: "Usuario",
      cell: ({ row, table }) => {
        const isMobile = (table.options.meta as { isMobile?: boolean })?.isMobile;
        const { userName, businessName } = row.original;
        if (isMobile) {
          return (
            <div className="flex flex-col">
              <span className="font-medium">{userName}</span>
              <span className="text-sm text-muted-foreground">{businessName}</span>
            </div>
          );
        }
        return userName;
      },
    },
    {
      accessorKey: "businessName",
      header: "Comercio",
      cell: ({ row }) => row.original.businessName,
    },
    {
      accessorKey: "amount",
      header: () => <div className="text-right">Monto</div>,
      cell: ({ row }) => <div className="text-right font-medium">{formatCurrency(row.original.amount)}</div>,
    },
    {
      accessorKey: "transactionDate",
      header: "Fecha",
      cell: ({ row }) => formatDateTime(row.original.transactionDate),
    },
    {
      accessorKey: "description",
      header: "Descripción",
      cell: ({ row }) => row.original.description ?? <span className="text-muted-foreground">-</span>,
    },
    {
      id: "actions",
      header: () => <div className="text-right">Acciones</div>,
      cell: ({ row }) => {
        const t = row.original;
        return (
          <div className="flex justify-end gap-2">
            <Button
              variant="ghost"
              size="icon"
              onClick={(e) => {
                e.stopPropagation();
                onEdit(t);
              }}
              title="Editar transacción">
              <Pencil className="h-4 w-4" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              onClick={(e) => {
                e.stopPropagation();
                onDelete(t.id);
              }}
              title="Eliminar transacción"
              className="text-destructive hover:text-destructive">
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        );
      },
    },
  ];
}
