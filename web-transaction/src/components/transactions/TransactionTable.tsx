"use client";

import { useState, useMemo } from "react";
import type { TransactionResponseDTO } from "@/types/transaction";
import { useMediaQuery } from "@/hooks/useMediaQuery";
import { getTransactionColumns } from "@/components/transactions/transactionColumns";
import { DataTable } from "@/components/ui/data-table";
import { TransactionDetailSheet } from "@/components/transactions/TransactionDetailSheet";

interface TransactionTableProps {
  transactions: TransactionResponseDTO[];
  onEdit: (transaction: TransactionResponseDTO) => void;
  onDelete: (id: number) => void;
  isLoading?: boolean;
}

const MOBILE_BREAKPOINT = "(max-width: 768px)";

/** Visibilidad de columnas en mobile: solo Usuario (con Comercio debajo) y Monto */
const MOBILE_COLUMN_VISIBILITY: Record<string, boolean> = {
  id: false,
  userName: true,
  businessName: false,
  amount: true,
  transactionDate: false,
  description: false,
  actions: false,
};

export function TransactionTable({ transactions, onEdit, onDelete, isLoading = false }: TransactionTableProps) {
  const isMobile = useMediaQuery(MOBILE_BREAKPOINT);
  const [detailTransaction, setDetailTransaction] = useState<TransactionResponseDTO | null>(null);
  const [detailSheetOpen, setDetailSheetOpen] = useState(false);

  const columns = useMemo(() => getTransactionColumns({ onEdit, onDelete }), [onEdit, onDelete]);

  const columnVisibility = isMobile ? MOBILE_COLUMN_VISIBILITY : undefined;
  const meta = { isMobile };

  const openDetailSheet = (transaction: TransactionResponseDTO) => {
    setDetailTransaction(transaction);
    setDetailSheetOpen(true);
  };

  const handleCloseDetailSheet = (open: boolean) => {
    setDetailSheetOpen(open);
    if (!open) setDetailTransaction(null);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-8">
        <p className="text-muted-foreground">Cargando transacciones...</p>
      </div>
    );
  }

  if (transactions.length === 0) {
    return (
      <div className="flex items-center justify-center py-8">
        <p className="text-muted-foreground">No hay transacciones disponibles</p>
      </div>
    );
  }

  return (
    <>
      <DataTable<TransactionResponseDTO, unknown>
        columns={columns}
        data={transactions}
        columnVisibility={columnVisibility}
        meta={meta}
        getRowProps={
          isMobile
            ? (row) => ({
                onClick: () => openDetailSheet(row),
                className: "cursor-pointer",
              })
            : undefined
        }
      />
      <TransactionDetailSheet open={detailSheetOpen} onOpenChange={handleCloseDetailSheet} transaction={detailTransaction} onEdit={onEdit} onDelete={onDelete} />
    </>
  );
}
