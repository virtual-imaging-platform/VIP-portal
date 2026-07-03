<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { Search } from 'lucide-vue-next'
import AppBadge from '@/components/ui/AppBadge.vue'
import AppCard from '@/components/ui/AppCard.vue'
import { useEnginesStore } from '@/stores/engines.store'
import { EngineStatus, EngineListParams } from '@/types/engine.types'
import { useFormatters } from '@/composables/useFormatters'

const enginesStore = useEnginesStore()
const { formatRelativeTime } = useFormatters()

const searchQuery = ref('')
const endpointFilter = ref('')
const statusFilter = ref('')

const statusColors = {
  enabled: 'primary',
  disabled: 'gray',
  Completed: 'success',
  Failed: 'danger',
  Killed: 'warning',
  Queued: 'info',
  Unknown: 'gray',
} as const

const statusList: EngineStatus[] = Object.values(EngineStatus)

const page = ref(0)
const pageSize = 20

function buildParams(): EngineListParams {
  const params: EngineListParams = {
    offset: page.value * pageSize,
    quantity: pageSize,
  }
  //TODO add name/status/endpoint filter
  //if totalCount >= page * pageSize => faire la recherche avec ce qui est dans le store, sinon fetch à nouveau
  if (searchQuery.value) params.search = searchQuery.value
  if (statusFilter.value) params.status = statusFilter.value
  if (endpointFilter.value) params.endDate = endpointFilter.value
  return params
}

async function loadEngines() {
  page.value = 0
  await enginesStore.fetchEngines(buildParams())
}

function onPage(delta: number) {
  page.value = Math.max(0, page.value + delta)
  enginesStore.fetchEngines(buildParams())
}

onMounted(loadEngines)
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Engines</h1>
      <p class="mt-1 text-sm text-gray-500">
        Browse (and manage, SOON!) your engine.
      </p>
    </div>

    <AppCard padding class="space-y-4">
      <div class="relative">
        <Search class="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-400" />
        <input
          v-model="searchQuery"
          type="search"
          placeholder="Search by engine name"
          class="block w-full rounded-lg border border-gray-300 py-2.5 pl-10 pr-4 text-sm placeholder:text-gray-400 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0"
          @input="loadEngines"
        />
      </div>
      <div class="flex flex-wrap items-end gap-3">
        <div>
          <label class="block text-xs font-medium text-gray-600">Status</label>
          <select
            v-model="statusFilter"
            class="mt-1 block rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500"
            @change="loadEngines"
          >
            <option value="">All</option>
            <option v-for="s in statusList" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-medium text-gray-600">Endpoint</label>
          <input
            v-model="endpointFilter"
            type="search"
            placeholder="Search by endpoint"
            class="block w-full rounded-lg border border-gray-300 py-2 pl-10 pr-4 text-sm placeholder:text-gray-400 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0"
            @change="loadEngines"
          />
        </div>
      </div>
    </AppCard>

    <AppCard :padding="false">
      <div v-if="enginesStore.isLoading" class="flex justify-center py-16 text-sm text-gray-500">
        Loading engines...
      </div>

      <div v-else-if="enginesStore.engines.length === 0" class="py-16 text-center text-sm text-gray-500">
        No engine found.
      </div>

      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200 text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Name</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Endpoint</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr
              v-for="eg in enginesStore.engines"
              :key="eg.name"
              class="transition hover:bg-gray-50"
            >
              <!-- <td class="px-4 py-3">
                  {{ eg.name }}
              </td> -->
              <td class="px-4 py-3 font-medium text-gray-900">
                {{ eg.name }}
              </td>
              <td class="px-4 py-3 text-gray-600">
                {{ eg.endpoint }}
                <!-- <span class="text-gray-400">v{{ eg.applicationVersion }}</span> -->
              </td>
              <td class="px-4 py-3">
                <AppBadge :variant="statusColors[eg.status] || 'gray'">
                  {{ eg.status }}
                </AppBadge>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </AppCard>

    <div class="flex items-center justify-between gap-4 text-sm text-gray-600">
        <span>Total: {{ enginesStore.totalCount }} engine(s)</span>
        <div class="flex items-center gap-2">
          <button
            :disabled="page === 0"
            class="rounded-lg border border-gray-300 px-3 py-1.5 disabled:opacity-40"
            @click="onPage(-1)"
          >
            Previous
          </button>
          <span class="font-medium">Page {{ page + 1 }}</span>
          <button
            :disabled="(page + 1) * pageSize >= enginesStore.total"
            class="rounded-lg border border-gray-300 px-3 py-1.5 disabled:opacity-40"
            @click="onPage(1)"
          >
            Next
          </button>
        </div>
      </div>
  </div>
</template>
