import apiClient from './client'
import type { UserListItemDTO } from '@/types/transaction'

/**
 * Servicio para operaciones de usuarios
 */
export const userService = {
  /**
   * Obtiene todos los usuarios
   * GET /user
   */
  async getAllUsers(): Promise<UserListItemDTO[]> {
    const response = await apiClient.get<UserListItemDTO[]>('/user')
    return response.data
  },
}
