import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useResourcesStore } from './resources.store'

const mocks = vi.hoisted(() => ({
  getAll: vi.fn(),
}))

vi.mock('@/api/resources.api', () => ({
  resourcesApi: {
    getAll: mocks.getAll,
  },
}))

describe('useResourcesStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('fetches and stores resources', async () => {
    mocks.getAll.mockResolvedValue({
      data: [
        { name: 'cluster-a', status: true, type: 'BATCH', configuration: 'queue=short', engines: [], groups: [] },
        { name: 'cluster-b', status: false, type: 'BATCH', configuration: '', engines: [], groups: [] },
      ],
      total: 2,
    })

    const store = useResourcesStore()
    expect(store.isLoading).toBe(false)

    const result = await store.fetchResources(0, 50)

    expect(mocks.getAll).toHaveBeenCalledWith(0, 50, undefined)
    expect(store.isLoading).toBe(false)
    expect(store.totalCount).toBe(2)
    expect(store.resources.map((resource) => resource.name)).toEqual(['cluster-a', 'cluster-b'])
    expect(result).toEqual(store.resources)
  })
})
