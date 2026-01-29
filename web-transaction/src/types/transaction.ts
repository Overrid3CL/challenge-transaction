/**
 * Tipos TypeScript basados en los DTOs de la API REST de transacciones
 */

/**
 * DTO de respuesta con los datos completos de una transacción
 */
export interface TransactionResponseDTO {
  id: number
  userId: number
  userName: string
  businessId: number
  businessName: string
  amount: number
  transactionDate: string // ISO 8601 date-time format
  description?: string
  createdAt: string // ISO 8601 date-time format
  updatedAt: string // ISO 8601 date-time format
}

/**
 * DTO para crear una nueva transacción
 */
export interface TransactionCreateDTO {
  userId: number
  businessId: number
  amount: number // mínimo 1
  transactionDate: string // ISO 8601 date-time format
  description?: string
}

/**
 * DTO para actualizar una transacción existente
 * Todos los campos son opcionales
 */
export interface TransactionUpdateDTO {
  userId?: number
  businessId?: number
  amount?: number // mínimo 1
  transactionDate?: string // ISO 8601 date-time format
  description?: string
}

/**
 * DTO de respuesta para errores de la API
 */
export interface ErrorResponseDTO {
  timestamp: string // ISO 8601 date-time format
  status: number
  error: string
  message: string
  path: string
  validationErrors?: Record<string, string>
}

/**
 * Tipo auxiliar para el modo del formulario
 */
export type TransactionFormMode = 'create' | 'edit'

/**
 * Tipo auxiliar para el estado de carga
 */
export type LoadingState = 'idle' | 'loading' | 'success' | 'error'

/**
 * DTO de ítem de listado de usuario (id, name)
 */
export interface UserListItemDTO {
  id: number
  name: string
}

/**
 * DTO de ítem de listado de negocio (id, name)
 */
export interface BusinessListItemDTO {
  id: number
  name: string
}

/**
 * DTO para el comercio top
 */
export interface TopBusinessDTO {
  businessId: number
  businessName: string
  transactionCount: number
}

/**
 * DTO de respuesta con estadísticas agregadas de transacciones
 */
export interface TransactionStatsDTO {
  volumen: number // Volumen: Total transaccionado (SUM de amount)
  foco: number // Foco: Ticket promedio (AVG de amount)
  control: number // Control: Cantidad de transacciones con monto mayor al umbral
  habito: TopBusinessDTO // Hábito: Comercio top con más transacciones
}
