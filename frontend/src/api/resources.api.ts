import { backendClient } from './client'
import type { PrecisePage } from '@/types/application.types'
import type { Resource } from '@/types/resource.types'

export type BackendResource = Resource

export const resourcesApi = {
  getAll: (offset = 0, quantity = 50, group?: string) => {
    const params: Record<string, number | string> = { offset, quantity }
    if (group) {
      params.group = group
    }

    return backendClient
      .get<PrecisePage<BackendResource>>('/internal/resources', { params })
      .then((r) => r.data)
  },
  create: (e: Resource) => {
    return backendClient.post<BackendResource>(`/internal/resources`, e).then((r) => r.data)
  },

  update: (e: Resource) => {
    return backendClient.put<BackendResource>(`/internal/resources/${e.name}`, e).then((r) => r.data)
  },

  delete: (e: Resource) => {
    return backendClient.delete<void>(`/internal/resources/${e.name}`).then((r) => r.data)
  },
}
