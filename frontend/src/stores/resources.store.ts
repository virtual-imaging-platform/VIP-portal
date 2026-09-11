import { ref } from 'vue'
import { defineStore } from 'pinia'
import { resourcesApi } from '@/api/resources.api'
import type { Resource } from '@/types/resource.types'
import { useNotificationsStore } from './notifications.store'

export const useResourcesStore = defineStore('resources', () => {
  const resources = ref<Resource[]>([])
  const totalCount = ref(0)
  const isLoading = ref(false)

  const notifications = useNotificationsStore()

  async function fetchResources(offset = 0, quantity = 50, group?: string): Promise<Resource[]> {
    isLoading.value = true
    try {
      const page = await resourcesApi.getAll(offset, quantity, group)
      resources.value = page.data
      totalCount.value = page.total
    } finally {
      isLoading.value = false
    }

    return resources.value
  }

  async function updateResource(resource: Resource) {
      try {
        const updated = await resourcesApi.update(resource)
        const idx = resources.value.findIndex((e) => e.name === updated.name)
        if (idx === -1) return //TODO better error
        resources.value.splice(idx, 1, updated)
        notifications.success(`Successfully updated resource ${updated.name}`)
      } catch (err: any) {
        notifications.error(err)
      }
    }
  
    async function addResource(resource: Resource) {
      try {
        const added = await resourcesApi.create(resource)
        resources.value.push(added)
        notifications.success(`Successfully created resource ${added.name}`)
      } catch (err: any) {
        notifications.error(err)
      }
    }
  
    async function removeResource(resource: Resource) {
      try {
        const res = await resourcesApi.delete(resource)
        const idx = resources.value.findIndex((e) => e.name === resource.name)
        if (idx === -1) return //TODO better error
        resources.value.splice(idx, 1)
        notifications.success(`Successfully removed resource ${resource.name}`)
      } catch (err: any) {
        notifications.error(err)
      }
    }

  return {
    resources,
    totalCount,
    isLoading,
    fetchResources,
    addResource,
    updateResource,
    removeResource,
  }
})
