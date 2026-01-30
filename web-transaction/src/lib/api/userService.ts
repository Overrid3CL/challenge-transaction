import apiClient from "./client";
import type { UserResponseDTO, UserCreateDTO, UserUpdateDTO } from "@/types/transaction";

/**
 * Servicio para operaciones CRUD de usuarios
 */
export const userService = {
  /**
   * Obtiene todos los usuarios (información completa)
   * GET /user
   */
  async getAllUsers(): Promise<UserResponseDTO[]> {
    const response = await apiClient.get<UserResponseDTO[]>("/user");
    return response.data;
  },

  /**
   * Obtiene un usuario por ID
   * GET /user/{id}
   */
  async getUserById(id: number): Promise<UserResponseDTO> {
    const response = await apiClient.get<UserResponseDTO>(`/user/${id}`);
    return response.data;
  },

  /**
   * Crea un nuevo usuario
   * POST /user
   */
  async createUser(data: UserCreateDTO): Promise<UserResponseDTO> {
    const response = await apiClient.post<UserResponseDTO>("/user", data);
    return response.data;
  },

  /**
   * Actualiza un usuario existente
   * PUT /user/{id}
   */
  async updateUser(id: number, data: UserUpdateDTO): Promise<UserResponseDTO> {
    const response = await apiClient.put<UserResponseDTO>(`/user/${id}`, data);
    return response.data;
  },

  /**
   * Elimina un usuario (soft delete)
   * DELETE /user/{id}
   */
  async deleteUser(id: number): Promise<void> {
    await apiClient.delete(`/user/${id}`);
  },
};
