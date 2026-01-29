import { create } from 'zustand'
import type { UserListItemDTO } from '@/types/transaction'
import { userService } from '@/lib/api/userService'

const CACHE_DURATION = 5 * 60 * 1000 // 5 minutes in milliseconds

export interface UserStore {
  users: UserListItemDTO[]
  loading: boolean
  error: string | null
  lastFetched: number | null
  fetchUsers: () => Promise<void>
}

export const useUserStore = create<UserStore>((set, get) => ({
  users: [],
  loading: false,
  error: null,
  lastFetched: null,

  fetchUsers: async () => {
    const { users, lastFetched } = get()
    const now = Date.now()

    if (users.length > 0 && lastFetched !== null && (now - lastFetched) < CACHE_DURATION) {
      return
    }

    set({ loading: true, error: null })

    try {
      const response = await userService.getAllUsers()
      set({
        users: response as UserListItemDTO[],
        lastFetched: now,
        loading: false,
        error: null,
      })
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Error al cargar los usuarios',
        loading: false,
      })
    }
  },
}))