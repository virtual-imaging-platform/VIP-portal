<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Plus, Trash2 } from 'lucide-vue-next'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import AppInput from '@/components/ui/AppInput.vue'
import AppBadge from '@/components/ui/AppBadge.vue'
import { useApplicationsStore } from '@/stores/applications.store'
import { useAppVersionsStore } from '@/stores/appversions.stores'
import { useNotificationsStore } from '@/stores/notifications.store'
import { useWorkflowsStore } from '@/stores/workflows.store'
import type { Application } from '@/types/application.types'
import type { AppVersion } from '@/types/appversion.types'
import type { WorkflowInputPayload } from '@/types/workflow.types'
import { rememberRecentApplication } from '@/utils/recentApplications'
import { useBoutiquesLaunchForm } from '@/composables/useBoutiquesLaunchForm'
import { useDuplicatedLaunchInputs } from '@/composables/useDuplicatedLaunchInputs'
import FileInputControl from '@/components/launch/FileInputControl.vue'

const route = useRoute()
const router = useRouter()
const applicationsStore = useApplicationsStore()
const appversionsStore = useAppVersionsStore()
const notificationsStore = useNotificationsStore()
const workflowsStore = useWorkflowsStore()

const application = ref<Application | null>(null)
const selectedVersion = ref<AppVersion | null>(null)

const isLoading = ref(false)
const loadError = ref('')

type LaunchSubmissionPayload = {
  applicationName: string
  version: string
  executionName: string
  resultsDirectory: string
  inputs: Array<{
    id: string
    values: Array<{
      instanceId: string
      value: unknown
    }>
  }>
}

const executionName = ref('')
const resultsDirectory = ref('')
const lastSubmissionPayload = ref<LaunchSubmissionPayload | null>(null)

const appName = computed(() => route.params.name as string)
const routeVersion = computed(() => (route.params.version as string | undefined) ?? '')
const {
  inputParams,
  formValues,
  formErrors,
  groupErrors,
  isInputAvailable,
  getInputDisabledReason,
  setInputTextValue,
  initializeFormValues,
  validateForm,
  onFileChange,
  resetValidation,
  clearForm,
} = useBoutiquesLaunchForm(selectedVersion)

const {
  canDuplicateInput,
  getInputInstanceIds,
  initializeInputInstances,
  addInputClone,
  removeInputClone,
  getInputValueForInstance,
  setInputValueForInstance,
  onFileChangeForInstance,
  setInputTextValueForInstance,
  buildSubmissionInputs,
} = useDuplicatedLaunchInputs(inputParams, formValues, onFileChange, setInputTextValue)

function buildSubmissionPayload(): LaunchSubmissionPayload | null {
  if (!application.value || !selectedVersion.value) return null

  return {
    applicationName: application.value.name,
    version: selectedVersion.value.version,
    executionName: executionName.value,
    resultsDirectory: resultsDirectory.value,
    inputs: buildSubmissionInputs(),
  }
}

async function loadVersion(versionName: string) {
  if (!versionName) {
    selectedVersion.value = null
    loadError.value = 'No version provided in the URL.'
    clearForm()
    return
  }

  try {
    loadError.value = ''
    selectedVersion.value = await appversionsStore.fetchAppVersion(appName.value, versionName)
    initializeFormValues()
    initializeInputInstances()
    resetValidation()
  } catch {
    selectedVersion.value = null
    clearForm()
    initializeInputInstances()
    loadError.value = 'Unable to load the requested version.'
  }
}

async function onLaunchSubmit() {
  if (!application.value || !selectedVersion.value) return
  if (!validateForm()) return

  const payload = buildSubmissionPayload()
  if (!payload) return

  lastSubmissionPayload.value = payload
  window.localStorage.setItem('vip.lastLaunchPayload', JSON.stringify(payload))

  rememberRecentApplication({
    name: application.value.name,
    fullName: application.value.fullName ?? undefined,
    version: selectedVersion.value.version,
  })

  const inputs: Record<string, WorkflowInputPayload> = {}
  for (const input of payload.inputs) {
    const values = input.values
      .filter((v) => v.value != null && v.value !== '')
      .map((v) => {
        if (v.value instanceof File) return v.value.name
        return String(v.value)
      })
    if (values.length > 0) {
      inputs[input.id] = { values }
    }
  }

  if (payload.resultsDirectory) {
    inputs['results-directory'] = { values: [payload.resultsDirectory] }
  }

  const workflow = await workflowsStore.launchWorkflow({
    applicationName: payload.applicationName,
    applicationVersion: payload.version,
    workflowName: payload.executionName,
    inputs,
  })

  if (workflow) {
    router.push({ name: 'workflow-detail', params: { id: workflow.id } })
  }
}

onMounted(async () => {
  isLoading.value = true
  loadError.value = ''

  try {
    application.value = await applicationsStore.getApplication(appName.value)
    await loadVersion(routeVersion.value)
  } catch {
    loadError.value = 'Unable to load the application.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div class="space-y-6">
    <RouterLink
      :to="{ name: 'application-detail', params: { name: appName } }"
      class="inline-flex items-center gap-2 text-sm font-medium text-gray-600 hover:text-primary-600"
    >
      <ArrowLeft class="h-4 w-4" />
      Back to details
    </RouterLink>

    <div v-if="isLoading" class="py-10 text-sm text-gray-500">Loading...</div>

    <div
      v-else-if="loadError"
      class="rounded-lg border border-red-200 bg-red-50 p-4 text-sm text-red-700"
    >
      {{ loadError }}
    </div>

    <div v-else-if="application && selectedVersion" class="space-y-6">
      <div class="flex flex-wrap items-center gap-3">
        <h1 class="text-2xl font-bold text-gray-900">
          Launch {{ application.fullName || application.name }}
        </h1>

        <AppBadge variant="primary">
          {{ selectedVersion.version }}
        </AppBadge>
      </div>

      <AppCard v-if="selectedVersion" padding>
        <form class="space-y-3" @submit.prevent="onLaunchSubmit">
          <h2 class="text-sm font-semibold text-gray-700">Execution form</h2>

          <div class="grid gap-3 md:grid-cols-2">
            <AppInput
              v-model="executionName"
              label="Execution name"
              placeholder="e.g. freesurfer-run-001"
              :required="true"
            />
            <div>
              <label class="mb-1 block text-sm font-medium text-gray-700">
                Results directory <span class="text-red-500">*</span>
              </label>
              <FileInputControl
                v-model="resultsDirectory"
                mode="folder"
                :disabled="false"
              />
            </div>
          </div>

          <div v-for="input in inputParams" :key="input.id" class="space-y-1 rounded-lg">
            <div class="flex items-center gap-3">
              <p class="text-sm font-medium text-gray-800 min-w-0">
                {{ input.name }}
                <span v-if="input.required" class="text-red-500">*</span>
              </p>
              <div class="flex items-center gap-2">
                <AppBadge v-if="!input.required" :variant="'gray'"> Optional </AppBadge>
                <AppBadge
                  :variant="
                    input.type === 'String'
                      ? 'blue'
                      : input.type === 'Number'
                        ? 'green'
                        : input.type === 'Boolean'
                          ? 'purple'
                          : input.type === 'File'
                            ? 'cyan'
                            : 'gray'
                  "
                >
                  {{ input.type }}
                </AppBadge>
              </div>
            </div>

            <p class="text-sm text-gray-500">
              {{ input.description }}
            </p>

            <p v-if="getInputDisabledReason(input)" class="text-xs text-amber-700">
              {{ getInputDisabledReason(input) }}
            </p>

            <p v-if="input.usesAbsolutePath" class="text-xs text-gray-500">
              This value must use an absolute path.
            </p>

            <div v-if="input.type === 'Boolean'" class="pt-1">
              <label class="inline-flex items-center gap-2 text-sm text-gray-700">
                <input
                  :checked="Boolean(formValues[input.id])"
                  type="checkbox"
                  :disabled="!isInputAvailable(input)"
                  class="h-4 w-4 rounded border-gray-300 text-primary-600 disabled:opacity-50"
                  @change="formValues[input.id] = ($event.target as HTMLInputElement).checked"
                />
                Active
              </label>
            </div>

            <div
              v-if="input.type !== 'Boolean'"
              v-for="instanceId in getInputInstanceIds(input.id)"
              :key="`${input.id}-${instanceId}`"
            >
              <div class="flex items-start gap-2">
                <div class="min-w-0 flex-1">
                  <div v-if="input.type === 'File'" class="pt-0.5">
                    <FileInputControl
                      :model-value="String(getInputValueForInstance(input.id, instanceId) ?? '')"
                      :disabled="!isInputAvailable(input)"
                      @update:model-value="setInputValueForInstance(input.id, instanceId, $event)"
                    />
                  </div>

                  <div v-else-if="input.possibleValues?.length" class="pt-0.5 space-y-1">
                    <select
                      class="block w-full rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:bg-gray-50 disabled:text-gray-500"
                      :disabled="!isInputAvailable(input)"
                      :required="input.required"
                      :value="String(getInputValueForInstance(input.id, instanceId) ?? '')"
                      @change="
                        setInputValueForInstance(
                          input.id,
                          instanceId,
                          ($event.target as HTMLSelectElement).value,
                        )
                      "
                    >
                      <option value="" :disabled="input.required">Select a value</option>
                      <option v-for="choice in input.possibleValues" :key="choice" :value="choice">
                        {{ choice }}
                      </option>
                    </select>
                  </div>

                  <div v-else>
                    <AppInput
                      :model-value="
                        (getInputValueForInstance(input.id, instanceId) as string | number) ?? ''
                      "
                      :type="input.type === 'Number' ? 'number' : 'text'"
                      :required="input.required"
                      :disabled="!isInputAvailable(input)"
                      :placeholder="input.defaultValue ? `Default: ${input.defaultValue}` : ''"
                      @update:model-value="setInputTextValueForInstance(input, instanceId, $event)"
                    />
                  </div>
                </div>

                <button
                  v-if="instanceId === '0' && canDuplicateInput(input.type)"
                  type="button"
                  class="mt-0.5 inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-md border border-gray-300 text-gray-600 transition hover:border-primary-300 hover:text-primary-600"
                  :disabled="!isInputAvailable(input)"
                  @click="addInputClone(input.id)"
                >
                  <Plus class="h-4 w-4" />
                </button>
                <button
                  v-else-if="instanceId !== '0'"
                  type="button"
                  class="mt-0.5 inline-flex h-8 w-8 shrink-0 items-center justify-center rounded-md border border-gray-300 text-gray-600 transition hover:border-red-300 hover:text-red-600"
                  @click="removeInputClone(input.id, instanceId)"
                >
                  <Trash2 class="h-4 w-4" />
                </button>
              </div>
            </div>

            <p v-if="formErrors[input.id]" class="text-xs text-red-600">
              {{ formErrors[input.id] }}
            </p>
          </div>

          <div v-if="groupErrors.length" class="rounded-lg border border-red-200 bg-red-50 p-3">
            <p class="text-sm font-semibold text-red-700">Group constraints</p>
            <ul class="mt-2 list-disc space-y-1 pl-5 text-sm text-red-700">
              <li v-for="error in groupErrors" :key="error">
                {{ error }}
              </li>
            </ul>
          </div>

          <AppButton type="submit" variant="primary" size="lg"> Launch app </AppButton>
        </form>
      </AppCard>
    </div>
  </div>
</template>
