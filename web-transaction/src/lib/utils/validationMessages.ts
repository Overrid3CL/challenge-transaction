/**
 * Traducciones de mensajes de validación devueltos por la API (inglés -> español).
 */
const TRANSACTION_VALIDATION_MESSAGES: Record<string, string> = {
  "Transaction date cannot be in the future": "La fecha de la transacción no puede ser futura",
};

/**
 * Traduce un mensaje de validación del backend al español si existe en el mapa.
 */
export function translateValidationMessage(message: string): string {
  return TRANSACTION_VALIDATION_MESSAGES[message] ?? message;
}
