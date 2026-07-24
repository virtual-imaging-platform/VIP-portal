<script setup lang="ts">
import { onMounted, ref, computed, watch } from 'vue'
import { Search, Plus } from 'lucide-vue-next'
import AppBadge from '@/components/ui/AppBadge.vue'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import { useEnginesStore } from '@/stores/engines.store'
import type { Engine, EngineStatus, EngineListParams } from '@/types/engine.types'

const enginesStore = useEnginesStore()

const searchFilter = ref('')
const statusFilter = ref('all')
const page = ref(0)
const pageSize = 20
const modalOpened = ref(false)
const isUpdating = ref(false)
const defaultEngineForm: Engine = { name: '', endpoint: '', status: 'disabled' as EngineStatus }
const editForm = ref<Engine>({ ...defaultEngineForm })

const statusColors = {
  enabled: 'primary',
  disabled: 'gray'
} as const

//TODO use enum and Object.values(EngineStatus) instead, actully cannot as status styling use type match
const statusList: string[] = ['enabled', 'disabled']


function buildParams(): EngineListParams {
  const params: EngineListParams = {
    offset: page.value * pageSize,
    quantity: pageSize,
  }
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

//TODO faster to have the filter logic here instead of backend-side
//TODO handle front pagination ?
const filteredEngines = computed(() => {
  if(statusFilter.value == "all") {
   return enginesStore.engines.filter(e => (e.name.includes(searchFilter.value ?? '') || e.endpoint.includes(searchFilter.value ?? '')))
  } else {
    return enginesStore.engines.filter(e => 
      (e.name.includes(searchFilter.value ?? '') || e.endpoint.includes(searchFilter.value ?? ''))
      && e.status === (statusFilter.value as EngineStatus)
    )
  }
})

//TODO currying to factor create/update ? and if needed elsewhere, make it a composable "CrudModal" (handling init form entity, create/update, notification, etc)
function openModal(e: Engine) {
  modalOpened.value = true
  editForm.value = { ...e }
}

function openCreateModal() {
  isUpdating.value = false
  openModal(defaultEngineForm)
}

function openUpdateModal(e: Engine) {
  isUpdating.value = true
  openModal(e)
}

async function submitForm() {
  if(isUpdating.value) {
    await enginesStore.updateEngine(editForm.value)
  } else {
    await enginesStore.addEngine(editForm.value)
  }
  //TODO close or let user close ?
  closeModal()
}

async function removeEngine() {
  await enginesStore.removeEngine(editForm.value)
  closeModal()
}

function closeModal(){
  modalOpened.value = false
  clearForm()
}

function clearForm() {
  editForm.value = { ...defaultEngineForm }
  isUpdating.value = false
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

    <AppButton class="mt-2" @click="openCreateModal">
        <Plus class="h-4 w-4" />
        Add engine
    </AppButton>

    <AppCard padding class="space-y-4">
      <div class="flex flex-wrap items-end gap-3">
        <div>
          <label class="block text-xs font-medium text-gray-600">Status</label>
          <select
            v-model="statusFilter"
            class="mt-1 block rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
            <option value="all">All</option>
            <option v-for="s in statusList" :key="s" :value="s">{{ s }}</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-medium text-gray-600">Search</label>
          <input
            v-model="searchFilter"
            type="search"
            placeholder="Search by name or endpoint"
            class="block w-full rounded-lg border border-gray-300 py-2 pl-4 pr-4 text-sm placeholder:text-gray-400 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0"
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
        <table class="table-auto min-w-full divide-y divide-gray-200 text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Name</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Endpoint</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr
              v-for="eg in filteredEngines"
              :key="eg.name"
              class="transition hover:bg-gray-50"
              @click="openUpdateModal(eg)"
            >
              <td class="px-4 py-3 font-medium text-gray-900">{{ eg.name }}</td>
              <td class="px-4 py-3 text-gray-600">{{ eg.endpoint }}</td>
              <td class="px-4 py-3">
                <AppBadge :variant="statusColors[eg.status] || 'gray'">{{ eg.status }}</AppBadge>
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
            :disabled="(page + 1) * pageSize >= enginesStore.totalCount"
            class="rounded-lg border border-gray-300 px-3 py-1.5 disabled:opacity-40"
            @click="onPage(1)"
          >
            Next
          </button>
        </div>
      </div>
  </div>
  <div
      v-if="modalOpened"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
      @click.self="closeModal"
    >
    <AppCard class="w-full max-w-3xl" padding>
        <div class="flex items-start justify-between gap-3">
          <div>
            <h2 class="text-lg font-semibold text-gray-900">
              {{ isUpdating ? 'Edit engine' : 'Add engine' }}
            </h2>
          </div>
          <button type="button" class="text-sm text-gray-500 hover:text-gray-700" @click="closeModal">Close</button>
        </div>

        <div class="mt-4 grid grid-cols-1 gap-3 md:grid-cols-3">
          <input v-model="editForm.name" :disabled="isUpdating" type="text" placeholder="Name *" class="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60" />
          <input v-model="editForm.endpoint" type="text" placeholder="Endpoint *" class="rounded-lg border border-gray-300 px-3 py-2 text-sm" />
          <select v-model="editForm.status" class="rounded-lg border border-gray-300 px-3 py-2 text-sm" >
            <option v-for="s in statusList" :key="s" :value="s" :selected="s === editForm.status">{{ s }}</option>
          </select>
        </div>

        <div class="mt-5 flex flex-wrap justify-end gap-2">
          <AppButton variant="secondary" @click="closeModal">
            Cancel
          </AppButton>
          <AppButton @click="submitForm">
            {{ isUpdating ? 'Update' : 'Create' }}
          </AppButton>
          <AppButton v-if="isUpdating" variant="danger" @click="removeEngine">
            delete
          </AppButton>
        </div>
      </AppCard>
  </div>
</template>
