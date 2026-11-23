import { ref } from 'vue'
import { defineStore } from 'pinia'
import { appVersionsApi, type BackendAppVersion } from '@/api/appVersions.api'
import type { AppVersion } from '@/types/appversion.types'

function toAppVersion(backend: BackendAppVersion): AppVersion {
  return {
    applicationName: backend.applicationName,
    version: backend.version,
    descriptor: backend.descriptor,
    parsedDescriptor: backend.parsedDescriptor,
    doi: backend.doi,
    visible: backend.visible,
    resources: backend.resources,
    tags: backend.tags,
    settings: backend.settings,
    source: backend.source,
    note: backend.note
  }
}

export const useAppVersionsStore = defineStore('appversions', () => {
  const appVersions = ref<AppVersion[]>([])
  const totalCount = ref(0)
  const isLoading = ref(false)
  const searchQuery = ref('')

  async function fetchAppVersions(): Promise<AppVersion[]> {
    isLoading.value = true
    try {
      const page = await appVersionsApi.getAll(0, 50)
      appVersions.value = page.data.map(toAppVersion)
      totalCount.value = page.total
    } finally {
      isLoading.value = false
    }
    return appVersions.value
  }

  async function fetchAppVersionsForApplication(appName: string): Promise<AppVersion[]> {
    isLoading.value = true
    try {
      const page = await appVersionsApi.getAllForApplication(appName, 0, 50)
      appVersions.value = page.data.map(toAppVersion)
      totalCount.value = page.total
    } finally {
      isLoading.value = false
    }
    return appVersions.value
  }

  async function fetchAppVersion(appName: string, version: string): Promise<AppVersion> {
    const backend = await appVersionsApi.getByVersion(appName, version)
    return toAppVersion(backend)
  }

  return {
    appVersions: appVersions,
    totalCount,
    isLoading,
    searchQuery,
    fetchAppVersions,
    fetchAppVersionsForApplication,
    fetchAppVersion,
  }
})
