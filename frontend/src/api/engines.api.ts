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
}
