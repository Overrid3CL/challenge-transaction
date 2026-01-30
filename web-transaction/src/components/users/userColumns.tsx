"use client";

import type { ColumnDef } from "@tanstack/react-table";
import type { UserResponseDTO } from "@/types/transaction";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";

export interface UserColumnsOptions {
  onEdit: (user: UserResponseDTO) => void;
  onDelete: (id: number) => void;
}

/**
 * Definiciones de columnas para la tabla de usuarios.
 * En móvil (meta.isMobile) se muestran solo nombre y correo (+ acciones).
 */
export function getUserColumns({ onEdit, onDelete }: UserColumnsOptions): ColumnDef<UserResponseDTO>[] {
  return [
    {
      accessorKey: "id",
      header: "ID",
      cell: ({ row }) => <span className="font-medium">{row.original.id}</span>,
    },
    {
      accessorKey: "name",
      header: "Nombre",
      cell: ({ row, table }) => {
        const isMobile = (table.options.meta as { isMobile?: boolean })?.isMobile;
        const { name, email } = row.original;
        if (isMobile) {
          return (
            <div className="flex flex-col">
              <span className="font-medium">{name}</span>
              <span className="text-sm text-muted-foreground">{email}</span>
            </div>
          );
        }
        return name;
      },
    },
    {
      accessorKey: "email",
      header: "Correo",
      cell: ({ row }) => row.original.email,
    },
    {
      accessorKey: "phone",
      header: "Teléfono",
      cell: ({ row }) => row.original.phone ?? <span className="text-muted-foreground">-</span>,
    },

    {
      id: "actions",
      header: () => <div className="text-right">Acciones</div>,
      cell: ({ row }) => {
        const u = row.original;
        return (
          <div className="flex justify-end gap-2">
            <Button
              variant="ghost"
              size="icon"
              onClick={(e) => {
                e.stopPropagation();
                onEdit(u);
              }}
              title="Editar usuario">
              <Pencil className="h-4 w-4" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              onClick={(e) => {
                e.stopPropagation();
                onDelete(u.id);
              }}
              title="Eliminar usuario"
              className="text-destructive hover:text-destructive">
              <Trash2 className="h-4 w-4" />
            </Button>
          </div>
        );
      },
    },
  ];
}
