import apiClient from './client'
import type { BusinessListItemDTO } from '@/types/transaction'

/**
 * Servicio para operaciones de negocios
 */
export const businessService = {
  /**
   * Obtiene todos los negocios
   * GET /business
   */
  async getAllBusinesses(): Promise<BusinessListItemDTO[]> {
    const response = await apiClient.get<BusinessListItemDTO[]>('/business')
    return response.data
  },
}
