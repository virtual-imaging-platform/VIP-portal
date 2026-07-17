import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useEnginesStore } from './engines.store'
import type { EngineListParams } from '@/types/engine.types'

const mocks = vi.hoisted(() => ({
  getAll: vi.fn(),
}))

vi.mock('@/api/engines.api', () => ({
  enginesApi: {
    getAll: mocks.getAll,
  },
}))

describe('useEnginesStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('fetches and stores engines', async () => {
    mocks.getAll.mockResolvedValue({
      data: [
        { name: 'e1', status: 'enabled', endpoint: 'http://localhost:5000' },
        { name: 'e2', status: 'disabled', endpoint: 'http://localhost:4999' },
      ],
      total: 2,
    })

    const store = useEnginesStore()
    expect(store.isLoading).toBe(false)

    const params: EngineListParams = { offset: 0, quantity: 50 }

    const result = await store.fetchEngines(params)

    expect(mocks.getAll).toHaveBeenCalledWith(params)
    expect(store.isLoading).toBe(false)
    expect(store.totalCount).toBe(2)
    expect(store.engines.map((engine) => engine.name)).toEqual(['e1', 'e2'])
    expect(result).toEqual(store.engines)
  })
})
