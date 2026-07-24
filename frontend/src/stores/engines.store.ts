import { ref } from 'vue'
import { defineStore } from 'pinia'
import { enginesApi } from '@/api/engines.api'
import type { Engine, EngineListParams } from '@/types/engine.types'
import { useNotificationsStore } from './notifications.store'

export const useEnginesStore = defineStore('engines', () => {
  const engines = ref<Engine[]>([])
  const totalCount = ref(0)
  const isLoading = ref(false)

  const notifications = useNotificationsStore()

  async function fetchEngines(params?: EngineListParams): Promise<void> {
    isLoading.value = true
    try {
      const page = await enginesApi.getAll(params?.offset, params?.quantity)
      engines.value = page.data
      totalCount.value = page.total
    } catch (err: any) {
      notifications.error(err)
    } finally {
      isLoading.value = false
    }
  }

  async function updateEngine(engine: Engine) {
    try {
      const updated = await enginesApi.update(engine)
      const idx = engines.value.findIndex((e) => e.name === updated.name)
      if (idx === -1) return //TODO better error
      engines.value.splice(idx, 1, updated)
      notifications.success(`Successfully updated engine ${updated.name}`)
    } catch (err: any) {
      notifications.error(err)
    }
  }

  async function addEngine(engine: Engine) {
    try {
      const added = await enginesApi.create(engine)
      engines.value.push(added)
      notifications.success(`Successfully created engine ${added.name}`)
    } catch (err: any) {
      notifications.error(err)
    }
  }

  async function removeEngine(engine: Engine) {
    try {
      const res = await enginesApi.delete(engine)
      const idx = engines.value.findIndex((e) => e.name === engine.name)
      if (idx === -1) return //TODO better error
      engines.value.splice(idx, 1)
      notifications.success(`Successfully removed engine ${engine.name}`)
    } catch (err: any) {
      notifications.error(err)
    }
  }


  return {
    engines,
    totalCount,
    isLoading,
    fetchEngines,
    addEngine,
    updateEngine,
    removeEngine,
  }
})
