import { useState, useEffect, useRef } from "react";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Card, CardContent } from "@/components/ui/card";
import { UserTable } from "@/components/users/UserTable";
import { UserForm } from "@/components/users/UserForm";
import { userService } from "@/lib/api/userService";
import type { UserResponseDTO, UserCreateDTO, UserUpdateDTO } from "@/types/transaction";
import { Plus } from "lucide-react";
import { ConfirmDeleteDialog } from "@/components/shared/ConfirmDeleteDialog";

export function UsersPage() {
  const [users, setUsers] = useState<UserResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<UserResponseDTO | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const isEditMode = selectedUser !== null;
  const isLoadingRef = useRef(false);

  const loadUsers = async () => {
    if (isLoadingRef.current) return;
    isLoadingRef.current = true;
    setIsLoading(true);
    setError(null);
    try {
      const data = await userService.getAllUsers();
      setUsers(data);
    } catch (err: unknown) {
      const e = err as {
        response?: { data?: { message?: string } };
        message?: string;
      };
      const errorMessage = e.response?.data?.message || e.message || "Error al cargar los usuarios";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al cargar usuarios:", err);
    } finally {
      setIsLoading(false);
      isLoadingRef.current = false;
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleCreate = () => {
    setSelectedUser(null);
    setIsDialogOpen(true);
    setError(null);
  };

  const handleEdit = (user: UserResponseDTO) => {
    setSelectedUser(user);
    setIsDialogOpen(true);
    setError(null);
  };

  const handleDelete = (id: number) => {
    setUserToDelete(id);
    setIsDeleteDialogOpen(true);
    setError(null);
  };

  const handleDeleteConfirm = async () => {
    setIsDeleting(true);
    setError(null);
    if (!userToDelete) {
      toast.error("No se ha seleccionado ningún usuario para eliminar");
      setIsDeleting(false);
      return;
    }
    try {
      await userService.deleteUser(userToDelete);
      await loadUsers();
      toast.success("Usuario eliminado correctamente");
      setIsDeleteDialogOpen(false);
      setUserToDelete(null);
    } catch (err: unknown) {
      const e = err as {
        response?: { data?: { message?: string } };
        message?: string;
      };
      const errorMessage = e.response?.data?.message || e.message || "Error al eliminar el usuario";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al eliminar usuario:", err);
    } finally {
      setIsDeleting(false);
    }
  };

  const handleSubmit = async (data: UserCreateDTO | UserUpdateDTO) => {
    setIsSubmitting(true);
    setError(null);
    try {
      if (isEditMode && selectedUser) {
        await userService.updateUser(selectedUser.id, data as UserUpdateDTO);
        toast.success("Usuario actualizado correctamente");
      } else {
        await userService.createUser(data as UserCreateDTO);
        toast.success("Usuario creado correctamente");
      }
      setIsDialogOpen(false);
      setSelectedUser(null);
      await loadUsers();
    } catch (err: unknown) {
      const e = err as {
        response?: {
          status?: number;
          data?: {
            message?: string;
            validationErrors?: Record<string, string>;
          };
        };
        message?: string;
      };
      const isConflictEmail = e.response?.status === 409;
      const errorMessage = e.response?.data?.message || e.message || (isEditMode ? "Error al actualizar el usuario" : "Error al crear el usuario");
      let messageToShow: string;
      if (isConflictEmail) {
        messageToShow = "Correo en uso";
      } else if (e.response?.data?.validationErrors) {
        messageToShow = `${errorMessage}: ${Object.values(e.response.data.validationErrors).join(", ")}`;
      } else {
        messageToShow = errorMessage;
      }
      setError(messageToShow);
      toast.error(messageToShow);
      console.error("Error al guardar usuario:", err);
      if (isConflictEmail) throw err;
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    setIsDialogOpen(false);
    setSelectedUser(null);
    setError(null);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold">Usuarios</h1>
          <p className="text-muted-foreground mt-1">Gestiona los usuarios del sistema</p>
        </div>
        <Button onClick={handleCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Nuevo usuario
        </Button>
      </div>

      {error && (
        <Card className="mb-4 border-destructive">
          <CardContent className="pt-6">
            <p className="text-destructive">{error}</p>
          </CardContent>
        </Card>
      )}

      <UserTable users={users} onEdit={handleEdit} onDelete={handleDelete} isLoading={isLoading} />

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{isEditMode ? "Editar usuario" : "Nuevo usuario"}</DialogTitle>
            <DialogDescription>{isEditMode ? "Modifica los campos que deseas actualizar. Todos los campos son opcionales." : "Completa el formulario para crear un nuevo usuario."}</DialogDescription>
          </DialogHeader>
          {error && (
            <div className="rounded-md bg-destructive/10 p-3">
              <p className="text-sm text-destructive">{error}</p>
            </div>
          )}
          <UserForm mode={isEditMode ? "edit" : "create"} initialData={selectedUser ?? undefined} onSubmit={handleSubmit} onCancel={handleCancel} isLoading={isSubmitting} />
        </DialogContent>
      </Dialog>

      <ConfirmDeleteDialog isOpen={isDeleteDialogOpen} onConfirm={handleDeleteConfirm} onCancel={() => setIsDeleteDialogOpen(false)} isLoading={isDeleting} title="¿Eliminar usuario?" description="Esta acción no se puede deshacer. El usuario quedará eliminado del sistema." />
    </div>
  );
}
