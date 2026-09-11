<script setup lang="ts">
import { onMounted, ref, computed, watch } from 'vue'
import { Search, Plus } from 'lucide-vue-next'
import AppBadge from '@/components/ui/AppBadge.vue'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import MultiSelect from '@/components/ui/MultiSelect.vue'
import { useResourcesStore } from '@/stores/resources.store'
import { type Resource, type ResourceType, ResourceTypeList } from '@/types/resource.types'
import type { Group } from '@/types/group.types'
import { useGroupsStore } from '@/stores/groups.store'

const resourcesStore = useResourcesStore()
const groupsStore = useGroupsStore()

const searchFilter = ref('')
const resourceTypeFilter = ref('')
const page = ref(0)
const pageSize = 20
const modalOpened = ref(false)
const isUpdating = ref(false)
const defaultResourceForm: Resource = {
  name : '',
  status : false,
  type : 'LOCAL' as ResourceType,
  configuration : '',
  engines : [],
  groups : [] as Group[]
}
const editForm = ref<Resource>({ ...defaultResourceForm })
const selectedResource = ref<Resource>()
const resourceStatusList = {
  "activated": true,
  "deactivated": false
}
  
  //TODO add type + status filter with string value like engine, so I can add an undefined to prevent filtering
const filteredResources = computed(() => {
  return resourcesStore.resources.filter(r => (r.name.includes(searchFilter.value ?? '')))
})

const hasChanged = computed(() => {
  if (!selectedResource.value || JSON.stringify(selectedResource.value) === JSON.stringify(editForm.value)) {
    return false
  }
  return true
})

function openModal(r: Resource) {
  modalOpened.value = true
  editForm.value = { ...r, groups: [...r.groups] }
}

function openCreateModal() {
  isUpdating.value = false
  selectedResource.value = defaultResourceForm
  openModal(defaultResourceForm)
}

function openUpdateModal(r: Resource) {
  isUpdating.value = true
  selectedResource.value = { ...r, groups: [...r.groups] }
  openModal(r)
}

async function submitForm() {
  if(!hasChanged) {
    closeModal()
    return
  }
  if(isUpdating.value) {
    await resourcesStore.updateResource(editForm.value)
  } else {
    await resourcesStore.addResource(editForm.value)
  }
  closeModal()
}

async function removeResource() {
  await resourcesStore.removeResource(editForm.value)
  closeModal()
}

function closeModal(){
  modalOpened.value = false
  clearForm()
}

function clearForm() {
  editForm.value = { ...defaultResourceForm }
  selectedResource.value = undefined
  isUpdating.value = false
}

async function loadResources() {
  page.value = 0
  await resourcesStore.fetchResources(page.value * pageSize, pageSize)
  await groupsStore.fetchResourceGroups()
}

async function onPage(delta: number) {
  page.value = Math.max(0, page.value + delta)
  await resourcesStore.fetchResources(page.value * pageSize, pageSize)
}

onMounted(loadResources)
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-gray-900">Resources</h1>
      <p class="mt-1 text-sm text-gray-500">
        Browse (and manage, SOON!) your resource.
      </p>
    </div>

    <AppButton class="mt-2" @click="openCreateModal">
        <Plus class="h-4 w-4" />
        Add resource
    </AppButton>

    <!-- TODO add by groups + engine filter ? => need to fetch them beforehand -->
    <AppCard padding class="space-y-4">
      <div class="flex flex-wrap items-end gap-3">
        <div>
          <label class="block text-xs font-medium text-gray-600">Search</label>
          <input
            v-model="searchFilter"
            type="search"
            placeholder="Search by name"
            class="block w-full rounded-lg border border-gray-300 py-2 pl-4 pr-4 text-sm placeholder:text-gray-400 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0"
          />
        </div>
      </div>
    </AppCard>

    <AppCard :padding="false">
      <div v-if="resourcesStore.isLoading" class="flex justify-center py-16 text-sm text-gray-500">
        Loading resources...
      </div>

      <div v-else-if="resourcesStore.resources.length === 0" class="py-16 text-center text-sm text-gray-500">
        No resource found.
      </div>

      <div v-else class="overflow-x-auto">
        <table class="table-auto min-w-full divide-y divide-gray-200 text-sm">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Name</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Status</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Type</th>
              <th class="px-4 py-3 text-left font-semibold text-gray-700">Configuration</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr
              v-for="eg in filteredResources"
              :key="eg.name"
              class="transition hover:bg-gray-50"
              @click="openUpdateModal(eg)"
            >
              <td class="px-4 py-3 font-medium text-gray-900">{{ eg.name }}</td>
              <td class="px-4 py-3">
                <AppBadge :variant="eg.status ? 'primary' : 'gray'">{{ eg.status }}</AppBadge>
              </td>
              <td class="px-4 py-3 text-gray-600">{{ eg.type }}</td>
              <td class="px-4 py-3 text-gray-600">{{ eg.configuration }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </AppCard>

    <div class="flex items-center justify-between gap-4 text-sm text-gray-600">
        <span>Total: {{ resourcesStore.totalCount }} resource(s)</span>
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
            :disabled="(page + 1) * pageSize >= resourcesStore.totalCount"
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
              {{ isUpdating ? 'Edit resource' : 'Add resource' }}
            </h2>
          </div>
          <button type="button" class="text-sm text-gray-500 hover:text-gray-700" @click="closeModal">Close</button>
        </div>

        <form id="submit-form" @submit.prevent="submitForm">
          <div class="mt-4 grid grid-cols-1 gap-3 md:grid-cols-3">
            <input v-model="editForm.name" required :disabled="isUpdating" type="text" placeholder="Name *" class="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60" />
            <!-- <select v-model="editForm.status" class="rounded-lg border border-gray-300 px-3 py-2 text-sm" >
              <option v-for="s in Object.keys(resourceStatusList)" :key="s" :value="s" :selected="s === editForm.status">{{ s }}</option>
            </select> -->
            <select v-model="editForm.type" class="rounded-lg border border-gray-300 px-3 py-2 text-sm" >
              <option v-for="s in ResourceTypeList" :key="s" :value="s" :selected="s === editForm.type">{{ s }}</option>
            </select>
            <input v-model="editForm.configuration" type="text" placeholder="Configuration" class="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60" />
            <MultiSelect
              v-model="editForm.groups"
              :options="groupsStore.groups"
              :option-value="'name'"
              :option-label="'name'"
              placeholder="Resource group"
            />
            <!-- <div class="relative flex flex-col overflow-y-scroll max-h-16 rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60">
              <label v-for="group in groupsStore.groups" :key="group.name">
                <input
                  v-model="editForm.groups"
                  type="checkbox"
                  :value="group"
                />
                {{ group.name }}
              </label>
            </div> -->
          </div>
        </form>

        <div class="mt-5 flex flex-wrap justify-end gap-2">
          <AppButton variant="secondary" @click="closeModal">
            Cancel
          </AppButton>
          <AppButton form="submit-form" type="submit" :disabled="!hasChanged">
            {{ isUpdating ? 'Update' : 'Create' }}
          </AppButton>
          <AppButton v-if="isUpdating" variant="danger" @click="removeResource">
            delete
          </AppButton>
        </div>
      </AppCard>
  </div>
</template>
