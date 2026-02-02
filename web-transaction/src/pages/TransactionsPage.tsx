import { useState, useEffect, useRef, useCallback } from "react";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { TransactionTable } from "@/components/transactions/TransactionTable";
import { TransactionForm } from "@/components/transactions/TransactionForm";
import { transactionService } from "@/lib/api/transactionService";
import { translateValidationMessage } from "@/lib/utils/validationMessages";
import type { TransactionResponseDTO, TransactionCreateDTO, TransactionUpdateDTO } from "@/types/transaction";
import { Plus, Search } from "lucide-react";
import { ConfirmDeleteDialog } from "@/components/shared/ConfirmDeleteDialog";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

const DEFAULT_PAGE_SIZE = 20;
const PAGE_SIZE_OPTIONS = [10, 20, 50] as const;
const DEBOUNCE_MS = 300;
const DEFAULT_SORT = { column: "transactionDate", direction: "desc" as const };

export function TransactionsPage() {
  const [transactions, setTransactions] = useState<TransactionResponseDTO[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(DEFAULT_PAGE_SIZE);
  const [sort, setSort] = useState<{ column: string; direction: "asc" | "desc" }>(DEFAULT_SORT);
  const [search, setSearch] = useState("");
  const [searchInput, setSearchInput] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState<TransactionResponseDTO | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [transactionToDelete, setTransactionToDelete] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [first, setFirst] = useState(true);
  const [last, setLast] = useState(true);

  const isEditMode = selectedTransaction !== null;
  const isLoadingRef = useRef(false);

  // Debounce searchInput -> search, reset page
  useEffect(() => {
    const t = setTimeout(() => {
      const s = searchInput.trim();
      setSearch(s);
      setPage(0);
    }, DEBOUNCE_MS);
    return () => clearTimeout(t);
  }, [searchInput]);

  const loadTransactionsPaginated = useCallback(async () => {
    if (isLoadingRef.current) return;
    isLoadingRef.current = true;
    setIsLoading(true);
    setError(null);
    try {
      const sortParam = `${sort.column},${sort.direction}`;
      const data = await transactionService.getTransactionsPaginated({
        page,
        size: pageSize,
        sort: sortParam,
        search: search || undefined,
      });
      setTransactions(data.content ?? []);
      setTotalElements(data.totalElements ?? 0);
      setTotalPages(data.totalPages ?? 1);
      setFirst(data.first ?? true);
      setLast(data.last ?? true);
    } catch (err: unknown) {
      const e = err as { response?: { data?: { message?: string } }; message?: string };
      const errorMessage = e.response?.data?.message || e.message || "Error al cargar las transacciones";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al cargar transacciones:", err);
    } finally {
      setIsLoading(false);
      isLoadingRef.current = false;
    }
  }, [page, pageSize, sort.column, sort.direction, search]);

  useEffect(() => {
    loadTransactionsPaginated();
  }, [loadTransactionsPaginated]);

  const handleSortChange = useCallback((column: string) => {
    setSort((prev) => {
      const same = prev.column === column;
      return {
        column,
        direction: same && prev.direction === "desc" ? "asc" : "desc",
      };
    });
    setPage(0);
  }, []);

  const handlePageSizeChange = (newSize: number) => {
    setPageSize(newSize);
    setPage(0);
  };

  const handleCreate = () => {
    setSelectedTransaction(null);
    setIsDialogOpen(true);
    setError(null);
  };

  const handleEdit = (transaction: TransactionResponseDTO) => {
    setSelectedTransaction(transaction);
    setIsDialogOpen(true);
    setError(null);
  };

  const handleDelete = (id: number) => {
    setTransactionToDelete(id);
    setIsDeleteDialogOpen(true);
    setError(null);
  };

  const handleDeleteConfirm = async () => {
    setIsDeleting(true);
    setError(null);
    if (!transactionToDelete) {
      toast.error("No se ha seleccionado ninguna transacción para eliminar");
      setIsDeleting(false);
      return;
    }
    try {
      await transactionService.deleteTransaction(transactionToDelete);
      await loadTransactionsPaginated();
      toast.success("Transacción eliminada correctamente");
      setIsDeleteDialogOpen(false);
      setTransactionToDelete(null);
    } catch (err: unknown) {
      const e = err as { response?: { data?: { message?: string } }; message?: string };
      const errorMessage = e.response?.data?.message || e.message || "Error al eliminar la transacción";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al eliminar transacción:", err);
    } finally {
      setIsDeleting(false);
    }
  };

  const handleSubmit = async (data: TransactionCreateDTO | TransactionUpdateDTO) => {
    setIsSubmitting(true);
    setError(null);
    try {
      if (isEditMode && selectedTransaction) {
        await transactionService.updateTransaction(selectedTransaction.id, data as TransactionUpdateDTO);
        toast.success("Transacción actualizada correctamente");
      } else {
        await transactionService.createTransaction(data as TransactionCreateDTO);
        toast.success("Transacción creada correctamente");
      }
      setIsDialogOpen(false);
      setSelectedTransaction(null);
      await loadTransactionsPaginated();
    } catch (err: unknown) {
      const e = err as { response?: { data?: { message?: string; validationErrors?: Record<string, string> } }; message?: string };
      const errorMessage = e.response?.data?.message || e.message || (isEditMode ? "Error al actualizar la transacción" : "Error al crear la transacción");
      let messageToShow: string;
      if (e.response?.data?.validationErrors) {
        const translated = Object.values(e.response.data.validationErrors).map(translateValidationMessage);
        messageToShow = translated.join(", ");
        setError(messageToShow);
      } else {
        messageToShow = errorMessage;
        toast.error(messageToShow);
      }
      console.error("Error al guardar transacción:", err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    setIsDialogOpen(false);
    setSelectedTransaction(null);
    setError(null);
  };

  const from = totalElements === 0 ? 0 : page * pageSize + 1;
  const to = Math.min((page + 1) * pageSize, totalElements);

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold">Transacciones</h1>
          <p className="text-muted-foreground mt-1">Gestiona tus transacciones financieras</p>
        </div>
        <Button onClick={handleCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Nueva Transacción
        </Button>
      </div>

      <div className="mb-4 flex flex-col gap-2 sm:flex-row sm:items-center sm:gap-4">
        <div className="relative flex-1 md:max-w-sm">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input placeholder="Buscar por usuario, comercio, descripción..." value={searchInput} onChange={(e) => setSearchInput(e.target.value)} className="pl-9" />
        </div>
      </div>

      <TransactionTable transactions={transactions ?? []} onEdit={handleEdit} onDelete={handleDelete} isLoading={isLoading} sort={sort} onSortChange={handleSortChange} emptyMessage={search ? "No se encontraron resultados" : "No hay transacciones disponibles"} />

      {!isLoading && (transactions ?? []).length > 0 && (
        <div className="mt-4 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <p className="text-sm text-muted-foreground">
            Mostrando {from}–{to} de {totalElements}
          </p>
          <div className="flex items-center gap-2 sm:gap-4">
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <span>Mostrar</span>
              <Select value={String(pageSize)} onValueChange={(v) => handlePageSizeChange(Number(v))}>
                <SelectTrigger className="w-[72px] h-9">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {PAGE_SIZE_OPTIONS.map((n) => (
                    <SelectItem key={n} value={String(n)}>
                      {n}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <span>por página</span>
            </div>
            <div className="flex gap-2">
              <Button variant="outline" size="sm" disabled={first} onClick={() => setPage((p) => Math.max(0, p - 1))}>
                Anterior
              </Button>
              <Button variant="outline" size="sm" disabled={last} onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}>
                Siguiente
              </Button>
            </div>
          </div>
        </div>
      )}

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{isEditMode ? "Editar Transacción" : "Nueva Transacción"}</DialogTitle>
            <DialogDescription>{isEditMode ? "Modifica los campos que deseas actualizar. Todos los campos son opcionales." : "Completa el formulario para crear una nueva transacción."}</DialogDescription>
          </DialogHeader>
          {error && (
            <div className="rounded-md bg-destructive/10 p-3">
              <p className="text-sm text-destructive">{error}</p>
            </div>
          )}
          <TransactionForm mode={isEditMode ? "edit" : "create"} initialData={selectedTransaction ?? undefined} onSubmit={handleSubmit} onCancel={handleCancel} isLoading={isSubmitting} />
        </DialogContent>
      </Dialog>

      <ConfirmDeleteDialog isOpen={isDeleteDialogOpen} onConfirm={handleDeleteConfirm} onCancel={() => setIsDeleteDialogOpen(false)} isLoading={isDeleting} title="¿Estás completamente seguro?" description="Esta acción no se puede deshacer. Esto eliminará permanentemente el registro." />
    </div>
  );
}
