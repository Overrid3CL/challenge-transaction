/**
 * Utilidades para formatear datos
 */

/**
 * Formatea un monto como moneda chilena (CLP)
 * @param amount - Monto en números
 * @returns Monto formateado como moneda (ej: "$45.000")
 */
export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount)
}

/**
 * Formatea una fecha en formato ISO 8601 a fecha legible
 * @param dateString - Fecha en formato ISO 8601
 * @returns Fecha formateada (ej: "20/01/2025")
 */
export function formatDate(dateString: string): string {
  const date = new Date(dateString)
  return new Intl.DateTimeFormat('es-CL', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  }).format(date)
}

/**
 * Formatea una fecha en formato ISO 8601 a fecha y hora legible
 * @param dateString - Fecha en formato ISO 8601
 * @returns Fecha y hora formateada (ej: "20/01/2025, 10:30")
 */
export function formatDateTime(dateString: string): string {
  const date = new Date(dateString)
  return new Intl.DateTimeFormat('es-CL', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  }).format(date)
}

/**
 * Convierte una fecha local a formato ISO 8601 para enviar al servidor
 * @param date - Objeto Date
 * @returns Fecha en formato ISO 8601
 */
export function toISOString(date: Date): string {
  return date.toISOString()
}

/**
 * Convierte un string de fecha (input datetime-local) a formato ISO 8601
 * @param dateTimeString - String de fecha en formato local (YYYY-MM-DDTHH:mm)
 * @returns Fecha en formato ISO 8601
 */
export function localDateTimeToISO(dateTimeString: string): string {
  const date = new Date(dateTimeString)
  return date.toISOString()
}

/**
 * Convierte una fecha ISO 8601 a formato para input datetime-local
 * @param isoString - Fecha en formato ISO 8601
 * @returns Fecha en formato local (YYYY-MM-DDTHH:mm)
 */
export function isoToLocalDateTime(isoString: string): string {
  const date = new Date(isoString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day}T${hours}:${minutes}`
}
