import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useEnginesStore } from './engines.store'
import type { Engine, EngineListParams } from '@/types/engine.types'

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
    
    const expected: Engine[] = [
        { name: 'e1', status: 'enabled', endpoint: 'http://localhost:5000' },
        { name: 'e2', status: 'disabled', endpoint: 'http://localhost:4999' },
      ]
    mocks.getAll.mockResolvedValue({
      data: expected,
      total: expected.length,
    })

    const store = useEnginesStore()
    expect(store.isLoading).toBe(false)

    const params: EngineListParams = { offset: 0, quantity: 30 }

    await store.fetchEngines(params)

    expect(mocks.getAll).toHaveBeenCalledWith(0, 30)
    expect(store.isLoading).toBe(false)
    expect(store.totalCount).toBe(expected.length)
    expect(store.engines.map((engine) => engine.name)).toEqual(['e1', 'e2'])
    expect(expected).toEqual(store.engines)
  })
})
