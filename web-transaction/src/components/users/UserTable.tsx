"use client";

import { useMemo } from "react";
import type { UserResponseDTO } from "@/types/transaction";
import { useMediaQuery } from "@/hooks/useMediaQuery";
import { getUserColumns } from "@/components/users/userColumns";
import { DataTable } from "@/components/ui/data-table";

interface UserTableProps {
  users: UserResponseDTO[];
  onEdit: (user: UserResponseDTO) => void;
  onDelete: (id: number) => void;
  isLoading?: boolean;
}

const MOBILE_BREAKPOINT = "(max-width: 768px)";

/** En móvil: solo nombre (con correo debajo) y acciones */
const MOBILE_COLUMN_VISIBILITY: Record<string, boolean> = {
  id: false,
  name: true,
  email: false,
  phone: false,
  userType: false,
  actions: true,
};

export function UserTable({ users, onEdit, onDelete, isLoading = false }: UserTableProps) {
  const isMobile = useMediaQuery(MOBILE_BREAKPOINT);

  const columns = useMemo(() => getUserColumns({ onEdit, onDelete }), [onEdit, onDelete]);

  const columnVisibility = isMobile ? MOBILE_COLUMN_VISIBILITY : undefined;
  const meta = { isMobile };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-8">
        <p className="text-muted-foreground">Cargando usuarios…</p>
      </div>
    );
  }

  if (users.length === 0) {
    return (
      <div className="flex items-center justify-center py-8">
        <p className="text-muted-foreground">No hay usuarios disponibles</p>
      </div>
    );
  }

  return <DataTable<UserResponseDTO, unknown> columns={columns} data={users} columnVisibility={columnVisibility} meta={meta} />;
}
