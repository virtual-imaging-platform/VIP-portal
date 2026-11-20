import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useEnginesStore } from './engines.store'
import type { Engine, EngineListParams } from '@/types/engine.types'
import { useNotificationsStore } from './notifications.store'

const mocks = vi.hoisted(() => {
  const initialEngines: Engine[] = [
    { name: 'e1', status: 'enabled', endpoint: 'http://localhost:5000' },
    { name: 'e2', status: 'disabled', endpoint: 'http://localhost:4999' },
  ]

  let engines = [...initialEngines]

  const reset = () => {
    engines = [...initialEngines]
  }
  return {
    engines: engines,
    getAll: vi.fn(async (offset, limit) => ({
      data: [...engines],
      total: engines.length,
    })),
    create: vi.fn(async (engine: Engine) => {
      const index = engines.findIndex((e) => e.name === engine.name)
      if (index !== -1) throw { response: { status: 400, data: { message: 'already exists' } } }
      engines.push(engine)
      return engine
    }),
    update: vi.fn(async (engine: Engine) => {
      const index = engines.findIndex((e) => e.name === engine.name)
      if (index === -1) {
        engines.push(engine)
      } else {
        engines.splice(index, 1, engine)
      }
      return engine
    }),
    delete: vi.fn(async (engine: Engine) => {
      if (engines.some((e) => e.name === engine.name)) {
        engines = engines.filter((e) => e.name !== engine.name)
      } else {
        throw { response: { status: 404, data: { message: 'not found' } } }
      }
    }),
  }
})

let store: ReturnType<typeof useEnginesStore>
let notificationStore: ReturnType<typeof useNotificationsStore>

vi.mock('@/api/engines.api', () => ({
  enginesApi: {
    getAll: mocks.getAll,
    create: mocks.create,
    update: mocks.update,
    delete: mocks.delete,
  },
}))

describe('useEnginesStore', () => {
  beforeAll(() => {
    setActivePinia(createPinia())
    store = useEnginesStore()
    notificationStore = useNotificationsStore()
    vi.clearAllMocks()
  })
  const e3: Engine = { name: 'e3', status: 'enabled', endpoint: 'http://localhost:6767' }
  it('fetches and stores engines', async () => {
    expect(store.isLoading).toBe(false)

    const params: EngineListParams = { offset: 0, quantity: 30 }

    await store.fetchEngines(params)

    expect(mocks.getAll).toHaveBeenCalledWith(0, 30)
    expect(store.isLoading).toBe(false)
    expect(store.totalCount).toBe(2)
    expect(store.engines.map((engine) => engine.name)).toEqual(['e1', 'e2'])
    expect(mocks.engines).toEqual(store.engines)
  })
  it('create OK', async () => {
    await store.addEngine(e3)
    expect(store.totalCount).toBe(3)
    expect(store.engines.map((engine) => engine.name)).toEqual(['e1', 'e2', 'e3'])
    expect(
      notificationStore.toasts.map((t) => t.message)[notificationStore.toasts.length - 1],
    ).toContain(`Successfully created engine ${e3.name}`)
  })
  it('create fail if already exists', async () => {
    await store.addEngine(e3)
    expect(
      notificationStore.toasts.map((t) => t.message)[notificationStore.toasts.length - 1],
    ).toBe(`Engine ${e3.name} already exists`)
    expect(store.totalCount).toBe(3)
  })
  it('update OK', async () => {
    await store.updateEngine({ ...e3, status: 'disabled' })
    expect(store.engines.filter((engine) => engine.name === e3.name)).not.toStrictEqual([e3])
    expect(store.engines.filter((engine) => engine.name === e3.name)).toStrictEqual([
      { ...e3, status: 'disabled' },
    ])
    expect(store.totalCount).toBe(3)
    expect(
      notificationStore.toasts.map((t) => t.message)[notificationStore.toasts.length - 1],
    ).toBe(`Successfully updated engine ${e3.name}`)
  })
  it('delete fail if not exists', async () => {
    let unsaved_engine: Engine = {
      name: 'non_existing_name',
      status: 'enabled',
      endpoint: 'le_endpoint',
    }
    await store.removeEngine(unsaved_engine)
    expect(
      notificationStore.toasts.map((t) => t.message)[notificationStore.toasts.length - 1],
    ).toBe(`Engine ${unsaved_engine.name} not found`)
    expect(store.totalCount).toBe(3)
  })
  it('delete OK', async () => {
    await store.removeEngine(e3)
    expect(
      notificationStore.toasts.map((t) => t.message)[notificationStore.toasts.length - 1],
    ).toBe(`Successfully removed engine ${e3.name}`)
    expect(store.engines.map((engine) => engine.name)).toEqual(['e1', 'e2'])
  })
})
