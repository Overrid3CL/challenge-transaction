import apiClient from "./client";
import type { TransactionResponseDTO, TransactionCreateDTO, TransactionUpdateDTO, TransactionStatsDTO, TransactionPageResponse } from "@/types/transaction";

/**
 * Servicio para operaciones CRUD de transacciones
 */
export const transactionService = {
  /**
   * Obtiene transacciones paginadas con ordenamiento y búsqueda
   * GET /transaction?page=&size=&sort=&search=
   * Si la API devuelve un array (formato legacy), se normaliza a TransactionPageResponse.
   */
  async getTransactionsPaginated(params: { page: number; size: number; sort?: string; search?: string }): Promise<TransactionPageResponse> {
    const { page, size, sort, search } = params;
    const searchParams = new URLSearchParams();
    searchParams.set("page", String(page));
    searchParams.set("size", String(size));
    if (sort) searchParams.set("sort", sort);
    if (search != null && search !== "") searchParams.set("search", search);
    const response = await apiClient.get<TransactionPageResponse | TransactionResponseDTO[]>(`/transaction?${searchParams.toString()}`);
    const data = response.data;
    if (Array.isArray(data)) {
      return {
        content: data,
        totalElements: data.length,
        totalPages: 1,
        size: data.length,
        number: 0,
        first: true,
        last: true,
        numberOfElements: data.length,
      };
    }
    return data as TransactionPageResponse;
  },

  /**
   * Obtiene una transacción por ID
   * GET /transaction/{id}
   */
  async getTransactionById(id: number): Promise<TransactionResponseDTO> {
    const response = await apiClient.get<TransactionResponseDTO>(`/transaction/${id}`);
    return response.data;
  },

  /**
   * Crea una nueva transacción
   * POST /transaction
   */
  async createTransaction(data: TransactionCreateDTO): Promise<TransactionResponseDTO> {
    const response = await apiClient.post<TransactionResponseDTO>("/transaction", data);
    return response.data;
  },

  /**
   * Actualiza una transacción existente
   * PUT /transaction/{id}
   */
  async updateTransaction(id: number, data: TransactionUpdateDTO): Promise<TransactionResponseDTO> {
    const response = await apiClient.put<TransactionResponseDTO>(`/transaction/${id}`, data);
    return response.data;
  },

  /**
   * Elimina una transacción (soft delete)
   * DELETE /transaction/{id}
   */
  async deleteTransaction(id: number): Promise<void> {
    await apiClient.delete(`/transaction/${id}`);
  },

  /**
   * Obtiene todas las transacciones de un usuario
   * GET /transaction/user/{userId}
   */
  async getTransactionsByUser(userId: number): Promise<TransactionResponseDTO[]> {
    const response = await apiClient.get<TransactionResponseDTO[]>(`/transaction/user/${userId}`);
    return response.data;
  },

  /**
   * Obtiene todas las transacciones de un negocio
   * GET /transaction/business/{businessId}
   */
  async getTransactionsByBusiness(businessId: number): Promise<TransactionResponseDTO[]> {
    const response = await apiClient.get<TransactionResponseDTO[]>(`/transaction/business/${businessId}`);
    return response.data;
  },

  /**
   * Obtiene estadísticas de transacciones
   * GET /transaction/stats
   */
  async getStatistics(): Promise<TransactionStatsDTO> {
    const response = await apiClient.get<TransactionStatsDTO>("/transaction/stats");
    return response.data;
  },
};
