<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { ChevronDown, ChevronRight, RefreshCw } from 'lucide-vue-next'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import LogViewer from '@/components/workflow/LogViewer.vue'
import { workflowsApi } from '@/api/workflows.api'
import {
  JOB_LOG_TYPES,
  JOB_STATE_ORDER,
  JOB_STATE_COLORS,
  TASK_STATUS_TO_STATE,
  type JobLogType,
  type JobState,
  type Task,
} from '@/types/workflow.types'

interface Props {
  wid: string
  isRunning: boolean
}

const props = withDefaults(defineProps<Props>(), {
  isRunning: false,
})

const jobs = ref<Task[]>([])
const loadError = ref('')
const jobsLoading = ref(false)
const selectedInvocationId = ref<number | null>(null)
const logContent = ref('')
const logLoading = ref(false)
const activeLogType = ref<JobLogType>('app-stdout')
const collapsedStates = ref(new Set<JobState>())

let pollTimer: ReturnType<typeof setInterval> | null = null

const isAutoRefreshActive = computed(() => props.isRunning)

const selectedJob = computed(
  () => jobs.value.find((j) => j.invocationID === selectedInvocationId.value) ?? null
)

const headerLabel = computed(() =>
  selectedJob.value
    ? `Job #${selectedJob.value.invocationID} — ${selectedJob.value.fileName}`
    : ''
)

const stateGroups = computed(() => {
  const groups: { state: JobState; tasks: Task[] }[] = []
  for (const state of JOB_STATE_ORDER) {
    const tasks = jobs.value.filter((j) => TASK_STATUS_TO_STATE[j.status] === state)
    if (tasks.length > 0) groups.push({ state, tasks })
  }
  const unknown = jobs.value.filter((j) => !(j.status in TASK_STATUS_TO_STATE))
  if (unknown.length > 0) {
    groups.push({ state: 'Unknown' as JobState, tasks: unknown })
  }
  return groups
})

function stateOf(task: Task): string {
  return TASK_STATUS_TO_STATE[task.status] ?? 'Unknown'
}

function toggleState(state: JobState | string) {
  const set = new Set(collapsedStates.value)
  if (set.has(state as JobState)) set.delete(state as JobState)
  else set.add(state as JobState)
  collapsedStates.value = set
}

async function fetchJobs() {
  if (jobs.value.length === 0) jobsLoading.value = true
  try {
    const list = await workflowsApi.listJobs(props.wid)
    jobs.value = list
    loadError.value = ''
    if (selectedInvocationId.value !== null
      && !jobs.value.some((j) => j.invocationID === selectedInvocationId.value)) {
      selectJob(null)
    }
  } catch (e) {
    // keep last known job list on refresh failure
    if (jobs.value.length === 0) {
      loadError.value = (e as { response?: { data?: { errorMessage?: string } } })?.response?.data?.errorMessage
        || 'Unable to load jobs.'
    }
  } finally {
    jobsLoading.value = false
  }
}

async function fetchLog() {
  if (selectedInvocationId.value === null) return
  logLoading.value = true
  try {
    logContent.value = await workflowsApi.readJobLog(props.wid, selectedInvocationId.value, activeLogType.value)
  } catch {
    // keep last known content on refresh failure
  } finally {
    logLoading.value = false
  }
}

function selectJob(invocationId: number | null) {
  selectedInvocationId.value = invocationId
  activeLogType.value = 'app-stdout'
  logContent.value = ''
  if (invocationId !== null) fetchLog()
}

function switchLogType(type: JobLogType) {
  if (activeLogType.value === type) return
  activeLogType.value = type
  logContent.value = ''
  fetchLog()
}

function startPolling() {
  stopPolling()
  if (!props.isRunning) return
  pollTimer = setInterval(() => {
    fetchJobs()
    fetchLog()
  }, 5000)
}

function stopPolling() {
  if (pollTimer !== null) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

watch(() => props.wid, () => {
  selectedInvocationId.value = null
  activeLogType.value = 'app-stdout'
  logContent.value = ''
  jobs.value = []
  collapsedStates.value = new Set()
  fetchJobs()
}, { immediate: true })

watch(isAutoRefreshActive, (active) => {
  if (active) startPolling()
  else stopPolling()
})

onMounted(() => {
  if (isAutoRefreshActive.value) startPolling()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<template>
  <AppCard padding>
    <div class="flex flex-col gap-4">
      <div class="flex flex-wrap items-center gap-2">
        <h2 class="text-sm font-semibold uppercase tracking-wider text-gray-500">Job Logs</h2>

        <div class="flex-1" />

        <span class="text-xs text-gray-400">{{ jobs.length }} job{{ jobs.length > 1 ? 's' : '' }}</span>

        <span v-if="isAutoRefreshActive" class="flex items-center gap-1 text-xs text-blue-600">
          <RefreshCw class="h-3 w-3 animate-spin" />
          Auto
        </span>
      </div>

      <div v-if="loadError" class="rounded-lg bg-red-50 px-3 py-2 text-xs text-red-600">
        {{ loadError }}
      </div>

      <div v-if="jobsLoading && jobs.length === 0 && !loadError" class="flex items-center gap-2 text-xs text-gray-500">
        <span class="h-3 w-3 animate-spin rounded-full border-2 border-gray-300 border-t-primary-600" />
        Loading jobs…
      </div>

      <div v-else-if="jobs.length === 0 && !loadError" class="text-xs text-gray-400">
        No jobs found for this workflow.
      </div>

      <div v-else class="flex flex-col gap-2">
        <div
          v-for="group in stateGroups"
          :key="group.state"
          class="overflow-hidden rounded-lg border border-gray-200"
        >
          <button
            type="button"
            class="flex w-full items-center gap-2 px-3 py-2 text-left hover:bg-gray-50"
            @click="toggleState(group.state)"
          >
            <ChevronRight v-if="collapsedStates.has(group.state)" class="h-3.5 w-3.5 text-gray-400" />
            <ChevronDown v-else class="h-3.5 w-3.5 text-gray-400" />
            <span
              class="inline-block h-2.5 w-2.5 rounded-full"
              :style="{ backgroundColor: JOB_STATE_COLORS[group.state] ?? '#9ca3af' }"
            />
            <span class="text-xs font-semibold text-gray-700">{{ group.state }}</span>
            <span class="rounded-full bg-gray-100 px-2 py-0.5 text-[10px] font-medium text-gray-500">
              {{ group.tasks.length }}
            </span>
          </button>

          <ul v-if="!collapsedStates.has(group.state)" class="divide-y divide-gray-100 border-t border-gray-100">
            <li v-for="task in group.tasks" :key="task.id">
              <button
                type="button"
                class="flex w-full items-center gap-2 px-6 py-1.5 text-left text-xs"
                :class="selectedInvocationId === task.invocationID ? 'bg-primary-50 text-primary-700' : 'text-gray-600 hover:bg-gray-50'"
                @click="selectJob(task.invocationID)"
              >
                <span class="font-medium">#{{ task.invocationID }}</span>
                <span class="truncate">{{ task.fileName }}</span>
                <span class="flex-1 truncate text-right text-[10px] text-gray-400">{{ task.command }}</span>
              </button>
            </li>
          </ul>
        </div>
      </div>

      <template v-if="selectedJob">
        <div class="text-xs font-medium text-gray-700">{{ headerLabel }}</div>

        <div class="flex flex-wrap gap-1">
          <AppButton
            v-for="logType in JOB_LOG_TYPES"
            :key="logType.value"
            variant="ghost"
            size="sm"
            :class="activeLogType === logType.value ? 'bg-gray-100 text-gray-900' : ''"
            @click="switchLogType(logType.value)"
          >
            {{ logType.label }}
          </AppButton>
        </div>

        <LogViewer
          :content="logContent"
          :loading="logLoading"
          :file-name="headerLabel"
        />
      </template>
    </div>
  </AppCard>
</template>
