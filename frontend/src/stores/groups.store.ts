import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { Group } from '@/types/group.types'
import { groupsApi } from '@/api/groups.api'

export const useGroupsStore = defineStore('groups', () => {
    const groups = ref<Group[]>([])
    const totalCount = ref(0)
    const isLoading = ref(false)

    async function fetchGroups(offset = 0, quantity = 50): Promise<Group[]> {
        isLoading.value = true
        try {
            const page = await groupsApi.getAll(false, false, offset, quantity)
            groups.value = page.data
            totalCount.value = page.total
        } finally {
            isLoading.value = false
        }

        return groups.value
    }

    async function fetchApplicationGroups(offset = 0, quantity = 50): Promise<Group[]> {
        isLoading.value = true
        try {
            const page = await groupsApi.getAll(true, false, offset, quantity)
            groups.value = page.data
            totalCount.value = page.total
        } finally {
            isLoading.value = false
        }

        return groups.value
    }

    async function fetchResourceGroups(offset = 0, quantity = 50): Promise<Group[]> {
        isLoading.value = true
        try {
            const page = await groupsApi.getAll(false, true, offset, quantity)
            groups.value = page.data
            totalCount.value = page.total
        } finally {
            isLoading.value = false
        }

        return groups.value
    }

    return {
        groups,
        totalCount,
        isLoading,
        fetchGroups,
        fetchApplicationGroups,
        fetchResourceGroups,
    }
})
