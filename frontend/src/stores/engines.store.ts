import { ref } from 'vue'
import { defineStore } from 'pinia'
import { enginesApi } from '@/api/engines.api'
import type { Engine } from '@/types/engine.types'

export const useEnginesStore = defineStore('engines', () => {
  const engines = ref<Engine[]>([])
  const totalCount = ref(0)
  const isLoading = ref(false)

  async function fetchEngines(offset = 0, quantity = 50): Promise<Engine[]> {
    isLoading.value = true
    try {
      const page = await enginesApi.getAll(offset, quantity)
      engines.value = page.data
      totalCount.value = page.total
    } finally {
      isLoading.value = false
    }

    return engines.value
  }

  return {
    engines,
    totalCount,
    isLoading,
    fetchEngines,
  }
})
