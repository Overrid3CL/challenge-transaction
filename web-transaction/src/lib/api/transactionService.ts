import apiClient from './client'
import type {
  TransactionResponseDTO,
  TransactionCreateDTO,
  TransactionUpdateDTO,
  TransactionStatsDTO,
} from '@/types/transaction'

/**
 * Servicio para operaciones CRUD de transacciones
 */
export const transactionService = {
  /**
   * Obtiene todas las transacciones
   * GET /transaction
   */
  async getAllTransactions(): Promise<TransactionResponseDTO[]> {
    const response = await apiClient.get<TransactionResponseDTO[]>('/transaction')
    return response.data
  },

  /**
   * Obtiene una transacción por ID
   * GET /transaction/{id}
   */
  async getTransactionById(id: number): Promise<TransactionResponseDTO> {
    const response = await apiClient.get<TransactionResponseDTO>(`/transaction/${id}`)
    return response.data
  },

  /**
   * Crea una nueva transacción
   * POST /transaction
   */
  async createTransaction(
    data: TransactionCreateDTO
  ): Promise<TransactionResponseDTO> {
    const response = await apiClient.post<TransactionResponseDTO>('/transaction', data)
    return response.data
  },

  /**
   * Actualiza una transacción existente
   * PUT /transaction/{id}
   */
  async updateTransaction(
    id: number,
    data: TransactionUpdateDTO
  ): Promise<TransactionResponseDTO> {
    const response = await apiClient.put<TransactionResponseDTO>(
      `/transaction/${id}`,
      data
    )
    return response.data
  },

  /**
   * Elimina una transacción (soft delete)
   * DELETE /transaction/{id}
   */
  async deleteTransaction(id: number): Promise<void> {
    await apiClient.delete(`/transaction/${id}`)
  },

  /**
   * Obtiene todas las transacciones de un usuario
   * GET /transaction/user/{userId}
   */
  async getTransactionsByUser(userId: number): Promise<TransactionResponseDTO[]> {
    const response = await apiClient.get<TransactionResponseDTO[]>(
      `/transaction/user/${userId}`
    )
    return response.data
  },

  /**
   * Obtiene todas las transacciones de un negocio
   * GET /transaction/business/{businessId}
   */
  async getTransactionsByBusiness(
    businessId: number
  ): Promise<TransactionResponseDTO[]> {
    const response = await apiClient.get<TransactionResponseDTO[]>(
      `/transaction/business/${businessId}`
    )
    return response.data
  },

  /**
   * Obtiene estadísticas de transacciones
   * GET /transaction/stats
   */
  async getStatistics(): Promise<TransactionStatsDTO> {
    const response = await apiClient.get<TransactionStatsDTO>('/transaction/stats')
    return response.data
  },
}
