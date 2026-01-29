import { useState, useEffect, useRef } from "react";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { TransactionTable } from "@/components/transactions/TransactionTable";
import { TransactionForm } from "@/components/transactions/TransactionForm";
import { transactionService } from "@/lib/api/transactionService";
import type { TransactionResponseDTO, TransactionCreateDTO, TransactionUpdateDTO } from "@/types/transaction";
import { Plus } from "lucide-react";
import { ConfirmDeleteDialog } from "@/components/shared/ConfirmDeleteDialog";

export function TransaccionesPage() {
  const [transactions, setTransactions] = useState<TransactionResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState<TransactionResponseDTO | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const isEditMode = selectedTransaction !== null;

  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [transactionToDelete, setTransactionToDelete] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  // Ref para evitar ejecuciones duplicadas (React StrictMode en desarrollo)
  const isLoadingRef = useRef(false);

  // Cargar transacciones
  useEffect(() => {
    loadTransactions();
  }, []);

  const loadTransactions = async () => {
    // Evitar ejecuciones duplicadas simultáneas
    if (isLoadingRef.current) {
      return;
    }

    isLoadingRef.current = true;
    setIsLoading(true);
    setError(null);
    try {
      const data = await transactionService.getAllTransactions();
      setTransactions(data);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || "Error al cargar las transacciones";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al cargar transacciones:", err);
    } finally {
      setIsLoading(false);
      isLoadingRef.current = false;
    }
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

  const handleDelete = async (id: number) => {
    setTransactionToDelete(id);
    setIsDeleteDialogOpen(true);
    setError(null);
    /*
    if (!window.confirm('¿Estás seguro de que deseas eliminar esta transacción?')) {
      return
    }

    try {
      await transactionService.deleteTransaction(id)
      // Recargar la lista después de eliminar
      await loadTransactions()
      setError(null)
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.message ||
        err.message ||
        'Error al eliminar la transacción'
      setError(errorMessage)
      console.error('Error al eliminar transacción:', err)
    }*/
  };

  const handleDeleteConfirm = async () => {
    setIsDeleting(true);
    setError(null);

    if (!transactionToDelete) {
      const msg = "No se ha seleccionado ninguna transacción para eliminar";
      setError(msg);
      toast.error(msg);
      setIsDeleting(false);
      return;
    }

    try {
      await transactionService.deleteTransaction(transactionToDelete as number);
      await loadTransactions();
      setError(null);
      toast.success("Transacción eliminada correctamente");
      setIsDeleteDialogOpen(false);
      setIsDeleting(false);
      setTransactionToDelete(null);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || "Error al eliminar la transacción";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al eliminar transacción:", err);
      setIsDeleting(false);
    }
  };

  const handleSubmit = async (data: TransactionCreateDTO | TransactionUpdateDTO) => {
    setIsSubmitting(true);
    setError(null);

    try {
      if (isEditMode && selectedTransaction) {
        // Actualizar transacción existente
        await transactionService.updateTransaction(selectedTransaction.id, data as TransactionUpdateDTO);
        toast.success("Transacción actualizada correctamente");
      } else {
        // Crear nueva transacción
        await transactionService.createTransaction(data as TransactionCreateDTO);
        toast.success("Transacción creada correctamente");
      }

      // Cerrar el dialog y recargar la lista
      setIsDialogOpen(false);
      setSelectedTransaction(null);
      await loadTransactions();
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || (isEditMode ? "Error al actualizar la transacción" : "Error al crear la transacción");

      // Si hay errores de validación, mostrarlos
      let messageToShow: string;
      if (err.response?.data?.validationErrors) {
        const validationErrors = Object.values(err.response.data.validationErrors).join(", ");
        messageToShow = `${errorMessage}: ${validationErrors}`;
      } else {
        messageToShow = errorMessage;
      }
      setError(messageToShow);
      toast.error(messageToShow);
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

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Transacciones</h1>
          <p className="text-muted-foreground mt-1">Gestiona tus transacciones financieras</p>
        </div>
        <Button onClick={handleCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Nueva Transacción
        </Button>
      </div>

      {error && (
        <Card className="mb-4 border-destructive">
          <CardContent className="pt-6">
            <p className="text-destructive">{error}</p>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Lista de Transacciones</CardTitle>
          <CardDescription>
            {transactions.length} transacción{transactions.length !== 1 ? "es" : ""} registrada{transactions.length !== 1 ? "s" : ""}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <TransactionTable transactions={transactions} onEdit={handleEdit} onDelete={handleDelete} isLoading={isLoading} />
        </CardContent>
      </Card>

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

          <TransactionForm mode={isEditMode ? "edit" : "create"} initialData={selectedTransaction || undefined} onSubmit={handleSubmit} onCancel={handleCancel} isLoading={isSubmitting} />
        </DialogContent>
      </Dialog>

      <ConfirmDeleteDialog isOpen={isDeleteDialogOpen} onConfirm={handleDeleteConfirm} onCancel={() => setIsDeleteDialogOpen(false)} isLoading={isDeleting} title="¿Estás completamente seguro?" description="Esta acción no se puede deshacer. Esto eliminará permanentemente el registro." />
    </div>
  );
}
