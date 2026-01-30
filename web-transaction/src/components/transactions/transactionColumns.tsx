"use client";

import type { ColumnDef } from "@tanstack/react-table";
import type { TransactionResponseDTO } from "@/types/transaction";
import { cn } from "@/lib/utils";
import { formatCurrency, formatDateTime } from "@/lib/utils/format";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";

export interface TransactionColumnsOptions {
  onEdit: (transaction: TransactionResponseDTO) => void;
  onDelete: (id: number) => void;
  sort?: { column: string; direction: "asc" | "desc" };
  onSortChange?: (column: string) => void;
}

const SORTABLE_COLUMNS = ["id", "userName", "businessName", "amount", "transactionDate", "description"];

function SortableHeader({ label, accessorKey, sort, onSortChange, alignRight }: { label: string; accessorKey: string; sort?: { column: string; direction: "asc" | "desc" }; onSortChange?: (column: string) => void; alignRight?: boolean }) {
  const active = sort?.column === accessorKey;
  const canSort = SORTABLE_COLUMNS.includes(accessorKey) && !!onSortChange;
  return (
    <button type="button" onClick={() => canSort && onSortChange?.(accessorKey)} className={cn("inline-flex w-full items-center gap-1 font-medium focus:outline-none focus:ring-2 focus:ring-ring rounded", canSort && "hover:underline cursor-pointer", !canSort && "cursor-default")}>
      {alignRight ? <span className="ml-auto">{label}</span> : label}
      {canSort && active && sort && (
        <span className="text-muted-foreground" aria-hidden>
          {sort.direction === "asc" ? "↑" : "↓"}
        </span>
      )}
    </button>
  );
}

/**
 * Devuelve las definiciones de columnas para la tabla de transacciones.
 * En mobile (meta.isMobile) la columna userName muestra también businessName debajo (Opción B).
 */
export function getTransactionColumns({ onEdit, onDelete, sort, onSortChange }: TransactionColumnsOptions): ColumnDef<TransactionResponseDTO>[] {
  return [
    {
      accessorKey: "id",
      header: () => <SortableHeader label="ID" accessorKey="id" sort={sort} onSortChange={onSortChange} />,
      cell: ({ row }) => <span className="font-medium">{row.original.id}</span>,
    },
    {
      accessorKey: "userName",
      header: () => <SortableHeader label="Usuario" accessorKey="userName" sort={sort} onSortChange={onSortChange} />,
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
      header: () => <SortableHeader label="Comercio" accessorKey="businessName" sort={sort} onSortChange={onSortChange} />,
      cell: ({ row }) => row.original.businessName,
    },
    {
      accessorKey: "amount",
      header: () => (
        <div className="flex w-full justify-end">
          <SortableHeader label="Monto" accessorKey="amount" sort={sort} onSortChange={onSortChange} />
        </div>
      ),
      cell: ({ row }) => <div className="text-right font-medium">{formatCurrency(row.original.amount)}</div>,
    },
    {
      accessorKey: "transactionDate",
      header: () => <SortableHeader label="Fecha" accessorKey="transactionDate" sort={sort} onSortChange={onSortChange} />,
      cell: ({ row }) => formatDateTime(row.original.transactionDate),
    },
    {
      accessorKey: "description",
      header: () => <SortableHeader label="Descripción" accessorKey="description" sort={sort} onSortChange={onSortChange} />,
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
