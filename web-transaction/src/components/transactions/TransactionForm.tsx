import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import type { TransactionCreateDTO, TransactionUpdateDTO, TransactionResponseDTO, TransactionFormMode } from "@/types/transaction";
import { isoToLocalDateTime } from "@/lib/utils/format";

interface TransactionFormProps {
  mode: TransactionFormMode;
  initialData?: TransactionResponseDTO;
  onSubmit: (data: TransactionCreateDTO | TransactionUpdateDTO) => Promise<void>;
  onCancel: () => void;
  isLoading?: boolean;
}

export function TransactionForm({ mode, initialData, onSubmit, onCancel, isLoading = false }: TransactionFormProps) {
  const [formData, setFormData] = useState<{
    userId: string;
    businessId: string;
    amount: string;
    transactionDate: string;
    description: string;
  }>({
    userId: initialData?.userId?.toString() || "",
    businessId: initialData?.businessId?.toString() || "",
    amount: initialData?.amount?.toString() || "",
    transactionDate: initialData ? isoToLocalDateTime(initialData.transactionDate) : "",
    description: initialData?.description || "",
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  // Actualizar formData cuando cambia initialData
  useEffect(() => {
    if (initialData) {
      setFormData({
        userId: initialData.userId.toString(),
        businessId: initialData.businessId.toString(),
        amount: initialData.amount.toString(),
        transactionDate: isoToLocalDateTime(initialData.transactionDate),
        description: initialData.description || "",
      });
    }
  }, [initialData]);

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (mode === "create") {
      if (!formData.userId || formData.userId.trim() === "") {
        newErrors.userId = "El usuario es requerido";
      } else if (isNaN(Number(formData.userId)) || Number(formData.userId) < 1) {
        newErrors.userId = "El ID de usuario debe ser un número válido";
      }

      if (!formData.businessId || formData.businessId.trim() === "") {
        newErrors.businessId = "El negocio es requerido";
      } else if (isNaN(Number(formData.businessId)) || Number(formData.businessId) < 1) {
        newErrors.businessId = "El ID de negocio debe ser un número válido";
      }

      if (!formData.amount || formData.amount.trim() === "") {
        newErrors.amount = "El monto es requerido";
      } else {
        const amountNum = Number(formData.amount);
        if (isNaN(amountNum) || amountNum < 1) {
          newErrors.amount = "El monto debe ser un número mayor o igual a 1";
        }
      }

      if (!formData.transactionDate || formData.transactionDate.trim() === "") {
        newErrors.transactionDate = "La fecha de transacción es requerida";
      }
    } else {
      // En modo edición, validar solo los campos que se envían
      if (formData.userId && formData.userId.trim() !== "" && (isNaN(Number(formData.userId)) || Number(formData.userId) < 1)) {
        newErrors.userId = "El ID de usuario debe ser un número válido";
      }
      if (formData.businessId && formData.businessId.trim() !== "" && (isNaN(Number(formData.businessId)) || Number(formData.businessId) < 1)) {
        newErrors.businessId = "El ID de negocio debe ser un número válido";
      }
      if (formData.amount && formData.amount.trim() !== "") {
        const amountNum = Number(formData.amount);
        if (isNaN(amountNum) || amountNum < 1) {
          newErrors.amount = "El monto debe ser un número mayor o igual a 1";
        }
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    try {
      if (mode === "create") {
        const createData: TransactionCreateDTO = {
          userId: Number(formData.userId),
          businessId: Number(formData.businessId),
          amount: Number(formData.amount),
          transactionDate: new Date(formData.transactionDate).toISOString(),
          description: formData.description || undefined,
        };
        await onSubmit(createData);
      } else {
        const updateData: TransactionUpdateDTO = {};
        if (formData.userId) updateData.userId = Number(formData.userId);
        if (formData.businessId) updateData.businessId = Number(formData.businessId);
        if (formData.amount) updateData.amount = Number(formData.amount);
        if (formData.transactionDate) {
          updateData.transactionDate = new Date(formData.transactionDate).toISOString();
        }
        if (formData.description !== undefined) {
          updateData.description = formData.description || undefined;
        }
        await onSubmit(updateData);
      }
    } catch (error) {
      console.error("Error al enviar formulario:", error);
    }
  };

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[field];
        return newErrors;
      });
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="userId">Usuario {mode === "create" && <span className="text-destructive">*</span>}</Label>
        <Input id="userId" type="number" min="1" value={formData.userId} onChange={(e) => handleChange("userId", e.target.value)} disabled={isLoading} aria-invalid={!!errors.userId} />
        {errors.userId && <p className="text-sm text-destructive">{errors.userId}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="businessId">Negocio {mode === "create" && <span className="text-destructive">*</span>}</Label>
        <Input id="businessId" type="number" min="1" value={formData.businessId} onChange={(e) => handleChange("businessId", e.target.value)} disabled={isLoading} aria-invalid={!!errors.businessId} />
        {errors.businessId && <p className="text-sm text-destructive">{errors.businessId}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="amount">Monto {mode === "create" && <span className="text-destructive">*</span>}</Label>
        <Input id="amount" type="number" min="1" value={formData.amount} onChange={(e) => handleChange("amount", e.target.value)} disabled={isLoading} aria-invalid={!!errors.amount} />
        {errors.amount && <p className="text-sm text-destructive">{errors.amount}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="transactionDate">Fecha de Transacción {mode === "create" && <span className="text-destructive">*</span>}</Label>
        <Input id="transactionDate" type="datetime-local" value={formData.transactionDate} onChange={(e) => handleChange("transactionDate", e.target.value)} disabled={isLoading} aria-invalid={!!errors.transactionDate} />
        {errors.transactionDate && <p className="text-sm text-destructive">{errors.transactionDate}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="description">Descripción (opcional)</Label>
        <Input id="description" type="text" value={formData.description} onChange={(e) => handleChange("description", e.target.value)} disabled={isLoading} placeholder="Descripción de la transacción" />
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
