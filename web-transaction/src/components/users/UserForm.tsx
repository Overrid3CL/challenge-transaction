import { useEffect } from "react";
import { Controller, useForm, type Resolver } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import type { UserCreateDTO, UserUpdateDTO, UserResponseDTO } from "@/types/transaction";
import { userCreateSchema, userUpdateSchema, USER_TYPE_VALUE, type UserCreateFormValues, type UserUpdateFormValues } from "@/lib/validations/user";

export type UserFormMode = "create" | "edit";

interface UserFormProps {
  mode: UserFormMode;
  initialData?: UserResponseDTO;
  onSubmit: (data: UserCreateDTO | UserUpdateDTO) => Promise<void>;
  onCancel: () => void;
  isLoading?: boolean;
}

function getDefaultValues(initialData?: UserResponseDTO): UserCreateFormValues & UserUpdateFormValues {
  return {
    name: initialData?.name ?? "",
    email: initialData?.email ?? "",
    phone: initialData?.phone ?? "",
    userType: (initialData?.userType ?? USER_TYPE_VALUE) as "USER",
  };
}

export function UserForm({ mode, initialData, onSubmit, onCancel, isLoading = false }: UserFormProps) {
  const form = useForm<UserCreateFormValues & UserUpdateFormValues>({
    resolver: zodResolver(mode === "create" ? userCreateSchema : userUpdateSchema) as Resolver<UserCreateFormValues & UserUpdateFormValues>,
    defaultValues: getDefaultValues(initialData),
  });

  useEffect(() => {
    if (initialData) {
      form.reset(getDefaultValues(initialData));
    }
  }, [initialData, form]);

  const handleSubmitForm = async (data: UserCreateFormValues & UserUpdateFormValues) => {
    try {
      if (mode === "create") {
        const createData: UserCreateDTO = {
          name: data.name.trim(),
          email: data.email.trim(),
          phone: data.phone?.trim() || undefined,
          userType: USER_TYPE_VALUE,
        };
        await onSubmit(createData);
      } else {
        const updateData: UserUpdateDTO = {};
        if (data.name?.trim()) updateData.name = data.name.trim();
        if (data.email?.trim()) updateData.email = data.email.trim();
        if (data.phone !== undefined) updateData.phone = data.phone?.trim() || undefined;
        if (data.userType?.trim()) updateData.userType = data.userType.trim();
        await onSubmit(updateData);
      }
    } catch (error: unknown) {
      const err = error as { response?: { status?: number; data?: { message?: string } } };
      const status = err.response?.status;
      const message = err.response?.data?.message ?? "";
      const isDuplicateEmail = status === 409 || message.includes("user_email_key") || message.includes("llave duplicada") || message.toLowerCase().includes("duplicate");
      if (isDuplicateEmail) {
        form.setError("email", { type: "manual", message: "Correo en uso" });
      }
      console.error("Error al enviar formulario:", error);
    }
  };

  return (
    <form onSubmit={form.handleSubmit(handleSubmitForm)} className="space-y-4">
      <div className="space-y-2">
        <Controller
          name="name"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="user-name">Nombre {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Input {...field} id="user-name" type="text" disabled={isLoading} aria-invalid={fieldState.invalid} placeholder="Nombre completo" />
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="email"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="user-email">Correo {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Input {...field} id="user-email" type="email" disabled={isLoading} aria-invalid={fieldState.invalid} placeholder="correo@ejemplo.com" />
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="phone"
          control={form.control}
          render={({ field }) => (
            <>
              <Label htmlFor="user-phone">Teléfono (opcional)</Label>
              <Input {...field} id="user-phone" type="text" disabled={isLoading} placeholder="+56912345678" />
            </>
          )}
        />
      </div>

      <div className="flex justify-end gap-2 pt-4">
        <Button type="button" variant="outline" onClick={onCancel} disabled={isLoading}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? "Guardando…" : mode === "create" ? "Crear" : "Actualizar"}
        </Button>
      </div>
    </form>
  );
}
