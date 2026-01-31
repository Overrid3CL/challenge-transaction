import { z } from "zod";

const NOT_FUTURE_DATE_MESSAGE = "La fecha de la transacción no puede ser posterior a hoy";

function isNotFutureDate(val: string): boolean {
  if (val === "") return true;
  return new Date(val) <= new Date();
}

/** Esquema para crear transacción: todos los campos obligatorios salvo descripción */
export const transactionCreateSchema = z.object({
  userId: z
    .string()
    .min(1, "El usuario es requerido")
    .refine((val) => !isNaN(Number(val)) && Number(val) >= 1 && Number.isInteger(Number(val)), "El ID de usuario debe ser un número válido"),
  businessId: z
    .string()
    .min(1, "El negocio es requerido")
    .refine((val) => !isNaN(Number(val)) && Number(val) >= 1 && Number.isInteger(Number(val)), "El ID de negocio debe ser un número válido"),
  amount: z
    .string()
    .min(1, "El monto es requerido")
    .refine((val) => !isNaN(Number(val)) && Number(val) >= 1, "El monto debe ser mayor o igual a 1"),
  transactionDate: z.string().min(1, "La fecha de transacción es requerida").refine(isNotFutureDate, NOT_FUTURE_DATE_MESSAGE),
  description: z.string().optional(),
});

/** Esquema para actualizar transacción: misma forma que create; campos pueden estar vacíos (no se envían) */
export const transactionUpdateSchema = z.object({
  userId: z.string().refine((val) => val === "" || (!isNaN(Number(val)) && Number(val) >= 1 && Number.isInteger(Number(val))), { message: "El ID de usuario debe ser un número válido" }),
  businessId: z.string().refine((val) => val === "" || (!isNaN(Number(val)) && Number(val) >= 1 && Number.isInteger(Number(val))), { message: "El ID de negocio debe ser un número válido" }),
  amount: z.string().refine((val) => val === "" || (!isNaN(Number(val)) && Number(val) >= 1), {
    message: "El monto debe ser mayor o igual a 1",
  }),
  transactionDate: z.string().refine(isNotFutureDate, NOT_FUTURE_DATE_MESSAGE),
  description: z.string().optional(),
});

export type TransactionCreateFormValues = z.infer<typeof transactionCreateSchema>;
export type TransactionUpdateFormValues = z.infer<typeof transactionUpdateSchema>;
