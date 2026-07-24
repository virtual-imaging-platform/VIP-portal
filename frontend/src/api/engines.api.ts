import { backendClient } from './client'
import type { PrecisePage } from '@/types/application.types'
import type { Engine } from '@/types/engine.types'

export type BackendEngine = Engine

export const enginesApi = {
  getAll: (offset = 0, quantity = 50) => {
    const params: Record<string, number> = { offset, quantity }
    return backendClient
      .get<PrecisePage<BackendEngine>>('/internal/engines', { params })
      .then((r) => r.data)
  },

  create: (e: Engine) => {
    return backendClient
      .post<BackendEngine>(`/internal/engines`, e)
      .then((r) => r.data)
  },

  update: (e: Engine) => {
    return backendClient
      .put<BackendEngine>(`/internal/engines/${e.name}`, e)
      .then((r) => r.data)
  },

  delete: (e: Engine) => {
    return backendClient
      .delete<void>(`/internal/engines/${e.name}`)
      .then((r) => r.data)
  },
}
