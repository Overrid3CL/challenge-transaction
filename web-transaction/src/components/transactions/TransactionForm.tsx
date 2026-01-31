import { useEffect, useMemo, useState } from "react";
import { Controller, useForm, type Resolver } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import type { TransactionCreateDTO, TransactionUpdateDTO, TransactionResponseDTO, TransactionFormMode, UserResponseDTO, BusinessListItemDTO } from "@/types/transaction";
import { isoToLocalDateTime } from "@/lib/utils/format";
import { transactionCreateSchema, transactionUpdateSchema } from "@/lib/validations/transaction";
import type { TransactionCreateFormValues } from "@/lib/validations/transaction";
import { userService } from "@/lib/api/userService";
import { businessService } from "@/lib/api/businessService";

interface TransactionFormProps {
  mode: TransactionFormMode;
  initialData?: TransactionResponseDTO;
  onSubmit: (data: TransactionCreateDTO | TransactionUpdateDTO) => Promise<void>;
  onCancel: () => void;
  isLoading?: boolean;
}

type TransactionFormValues = TransactionCreateFormValues;

function getDefaultValues(initialData?: TransactionResponseDTO): TransactionFormValues {
  return {
    userId: initialData?.userId?.toString() ?? "",
    businessId: initialData?.businessId?.toString() ?? "",
    amount: initialData?.amount?.toString() ?? "",
    transactionDate: initialData ? isoToLocalDateTime(initialData.transactionDate) : isoToLocalDateTime(new Date().toISOString()),
    description: initialData?.description ?? "",
  };
}

export function TransactionForm({ mode, initialData, onSubmit, onCancel, isLoading = false }: TransactionFormProps) {
  const [users, setUsers] = useState<UserResponseDTO[]>([]);
  const [businesses, setBusinesses] = useState<BusinessListItemDTO[]>([]);
  const [optionsLoading, setOptionsLoading] = useState(true);

  const form = useForm<TransactionFormValues>({
    resolver: zodResolver(mode === "create" ? transactionCreateSchema : transactionUpdateSchema) as Resolver<TransactionFormValues>,
    defaultValues: getDefaultValues(initialData),
  });

  useEffect(() => {
    let cancelled = false;
    setOptionsLoading(true);
    Promise.all([userService.getAllUsers(), businessService.getAllBusinesses()])
      .then(([usersData, businessesData]) => {
        if (!cancelled) {
          setUsers(usersData ?? []);
          setBusinesses(businessesData ?? []);
        }
      })
      .catch((err) => {
        if (!cancelled) {
          const message = (err as { response?: { data?: { message?: string } }; message?: string })?.response?.data?.message ?? (err as Error)?.message ?? "Error al cargar opciones";
          toast.error(message);
        }
      })
      .finally(() => {
        if (!cancelled) setOptionsLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, []);

  useEffect(() => {
    if (initialData) {
      form.reset(getDefaultValues(initialData));
    }
  }, [initialData, form]);

  // Incluir usuario de la transacción en las opciones si el API no lo devuelve (ej. usuario eliminado)
  const usersForSelect = useMemo(() => {
    const list = [...users];
    if (initialData?.userId != null && initialData?.userName != null && !list.some((u) => u.id === initialData.userId)) {
      list.unshift({ id: initialData.userId, name: initialData.userName, email: "", userType: "USER" } as UserResponseDTO);
    }
    return list;
  }, [users, initialData?.userId, initialData?.userName]);

  const handleSubmitForm = async (data: TransactionFormValues) => {
    try {
      if (mode === "create") {
        const createData: TransactionCreateDTO = {
          userId: Number(data.userId),
          businessId: Number(data.businessId),
          amount: Number(data.amount),
          transactionDate: new Date(data.transactionDate).toISOString(),
          description: data.description || undefined,
        };
        await onSubmit(createData);
      } else {
        const updateData: TransactionUpdateDTO = {};
        if (data.userId?.trim()) updateData.userId = Number(data.userId);
        if (data.businessId?.trim()) updateData.businessId = Number(data.businessId);
        if (data.amount?.trim()) updateData.amount = Number(data.amount);
        if (data.transactionDate?.trim()) {
          updateData.transactionDate = new Date(data.transactionDate).toISOString();
        }
        if (data.description !== undefined) {
          updateData.description = data.description || undefined;
        }
        await onSubmit(updateData);
      }
    } catch (error) {
      console.error("Error al enviar formulario:", error);
    }
  };

  return (
    <form onSubmit={form.handleSubmit(handleSubmitForm)} className="space-y-4">
      <div className="space-y-2">
        <Controller
          name="userId"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="userId">Usuario {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Select value={field.value} onValueChange={field.onChange} disabled={isLoading || optionsLoading} aria-invalid={fieldState.invalid}>
                <SelectTrigger id="userId" className="w-full" aria-invalid={fieldState.invalid}>
                  <SelectValue placeholder={optionsLoading ? "Cargando..." : "Seleccione usuario"} />
                </SelectTrigger>
                <SelectContent>
                  {usersForSelect.map((user) => (
                    <SelectItem key={user.id} value={String(user.id)}>
                      {user.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="businessId"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="businessId">Comercio {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Select value={field.value} onValueChange={field.onChange} disabled={isLoading || optionsLoading} aria-invalid={fieldState.invalid}>
                <SelectTrigger id="businessId" className="w-full" aria-invalid={fieldState.invalid}>
                  <SelectValue placeholder={optionsLoading ? "Cargando..." : "Seleccione comercio"} />
                </SelectTrigger>
                <SelectContent>
                  {businesses.map((business) => (
                    <SelectItem key={business.id} value={String(business.id)}>
                      {business.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="amount"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="amount">Monto {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Input {...field} id="amount" type="number" min={1} disabled={isLoading} aria-invalid={fieldState.invalid} />
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="transactionDate"
          control={form.control}
          render={({ field, fieldState }) => (
            <>
              <Label htmlFor="transactionDate">Fecha de Transacción {mode === "create" && <span className="text-destructive">*</span>}</Label>
              <Input {...field} id="transactionDate" type="datetime-local" max={isoToLocalDateTime(new Date().toISOString())} disabled={isLoading} aria-invalid={fieldState.invalid} />
              {fieldState.invalid && fieldState.error?.message && <p className="text-sm text-destructive">{fieldState.error.message}</p>}
            </>
          )}
        />
      </div>

      <div className="space-y-2">
        <Controller
          name="description"
          control={form.control}
          render={({ field }) => (
            <>
              <Label htmlFor="description">Descripción (opcional)</Label>
              <Input {...field} id="description" type="text" disabled={isLoading} placeholder="Descripción de la transacción" />
            </>
          )}
        />
      </div>

      <div className="flex justify-end gap-2 pt-4">
        <Button type="button" variant="outline" onClick={onCancel} disabled={isLoading}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? "Guardando..." : mode === "create" ? "Crear" : "Actualizar"}
        </Button>
      </div>
    </form>
  );
}
