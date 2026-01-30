import { z } from "zod";

/** Valores permitidos por la API/BD: USER o ADMIN */
const USER_TYPE_VALUE = "USER";

/** Esquema para crear usuario: name, email y userType requeridos; phone opcional */
export const userCreateSchema = z.object({
  name: z.string().min(1, "El nombre es requerido").max(100, "El nombre no puede exceder 100 caracteres"),
  email: z.string().min(1, "El correo es requerido").email("El correo debe ser válido").max(100),
  phone: z.string().max(20).optional().or(z.literal("")),
  userType: z.literal(USER_TYPE_VALUE, { message: "El tipo de usuario debe ser USER" }),
});

/** Esquema para actualizar usuario: todos los campos opcionales */
export const userUpdateSchema = z.object({
  name: z.string().min(1, "El nombre no puede estar vacío").max(100).optional().or(z.literal("")),
  email: z.string().email("El correo debe ser válido").max(100).optional().or(z.literal("")),
  phone: z.string().max(20).optional().or(z.literal("")),
  userType: z.string().max(20).optional().or(z.literal("")),
});

export type UserCreateFormValues = z.infer<typeof userCreateSchema>;
export type UserUpdateFormValues = z.infer<typeof userUpdateSchema>;

export { USER_TYPE_VALUE };
