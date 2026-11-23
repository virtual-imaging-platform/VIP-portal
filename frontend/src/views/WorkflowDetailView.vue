<script setup lang="ts">
import { onMounted, onUnmounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ArrowLeft,
  Play,
  Trash2,
  XCircle,
  Loader2,
  CheckCircle,
  Clock,
  HelpCircle,
  RefreshCw,
} from 'lucide-vue-next'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import WorkflowLogs from '@/components/workflow/WorkflowLogs.vue'
import JobLogs from '@/components/workflow/JobLogs.vue'
import { useWorkflowsStore } from '@/stores/workflows.store'
import { useNotificationsStore } from '@/stores/notifications.store'
import { useFormatters } from '@/composables/useFormatters'
import type { WorkflowStatus } from '@/types/workflow.types'

const POLL_INTERVAL_MS = 5000

const terminalStatuses: WorkflowStatus[] = ['Completed', 'Failed', 'Killed', 'Cleaned']

const route = useRoute()
const router = useRouter()
const workflowsStore = useWorkflowsStore()
const notifications = useNotificationsStore()
const { formatRelativeTime } = useFormatters()

const wid = computed(() => route.params.id as string)
const wf = computed(() => workflowsStore.currentWorkflow)

let pollTimer: ReturnType<typeof setInterval> | null = null

const statusMeta = computed(() => {
  const map: Record<WorkflowStatus, { label: string; bg: string; icon: string; pulse?: boolean }> = {
    Running: {
      label: 'Running',
      bg: 'bg-blue-50 border-blue-200',
      icon: 'spinner',
      pulse: true,
    },
    Queued: {
      label: 'Queued',
      bg: 'bg-sky-50 border-sky-200',
      icon: 'clock',
    },
    Completed: {
      label: 'Completed',
      bg: 'bg-emerald-50 border-emerald-200',
      icon: 'check',
    },
    Failed: {
      label: 'Failed',
      bg: 'bg-red-50 border-red-200',
      icon: 'x',
    },
    Killed: {
      label: 'Killed',
      bg: 'bg-amber-50 border-amber-200',
      icon: 'x',
    },
    Cleaned: {
      label: 'Cleaned',
      bg: 'bg-gray-50 border-gray-200',
      icon: 'trash',
    },
    Unknown: {
      label: 'Unknown',
      bg: 'bg-gray-50 border-gray-200',
      icon: 'help',
    },
  }
  return map[wf.value?.status ?? 'Unknown']
})

const isTerminal = computed(() => wf.value && terminalStatuses.includes(wf.value.status))
const canKill = computed(() => wf.value != null && ['Running', 'Queued', 'Unknown'].includes(wf.value.status))
const canClean = computed(() => wf.value != null && ['Completed', 'Failed', 'Killed'].includes(wf.value.status))
const isPolling = computed(() => !isTerminal.value && wf.value != null)

function startPolling() {
  stopPolling()
  if (!wf.value || terminalStatuses.includes(wf.value.status)) return
  pollTimer = setInterval(async () => {
    await workflowsStore.fetchWorkflow(wid.value)
    if (wf.value && terminalStatuses.includes(wf.value.status)) {
      stopPolling()
    }
  }, POLL_INTERVAL_MS)
}

function stopPolling() {
  if (pollTimer !== null) {
    clearInterval(pollTimer)
    pollTimer = null
  }
}

watch(isTerminal, (terminal) => {
  if (terminal) stopPolling()
})

async function handleKill() {
  if (!wf.value) return
  const ok = await workflowsStore.killWorkflow(wf.value.id)
  if (ok) notifications.success('Workflow has been killed.')
}

async function handleClean() {
  if (!wf.value) return
  const ok = await workflowsStore.cleanWorkflow(wf.value.id, true)
  if (ok) notifications.success('Workflow has been cleaned.')
}

function goToRelaunch() {
  if (!wf.value) return
  router.push({
    name: 'application-launch',
    params: {
      name: wf.value.applicationName,
      version: wf.value.applicationVersion,
    },
  })
}

onMounted(async () => {
  await workflowsStore.fetchWorkflow(wid.value)
  startPolling()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<template>
  <div class="space-y-6">
    <button
      class="inline-flex items-center gap-2 text-sm font-medium text-gray-600 hover:text-primary-600"
      @click="router.push('/executions')"
    >
      <ArrowLeft class="h-4 w-4" />
      Back to executions
    </button>

    <div v-if="workflowsStore.isLoading && !wf" class="flex justify-center py-16 text-sm text-gray-500">
      Loading execution...
    </div>

    <div v-else-if="!wf" class="py-16 text-center text-sm text-gray-500">
      Execution not found.
    </div>

    <template v-else>
      <div
        :class="[
          'rounded-xl border p-6',
          statusMeta.bg,
        ]"
      >
        <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div class="flex items-center gap-4">
            <div
              :class="[
                'flex h-14 w-14 items-center justify-center rounded-full',
                isPolling ? 'bg-blue-100 text-blue-600' : 'bg-white text-gray-600',
              ]"
            >
              <Loader2
                v-if="statusMeta.icon === 'spinner'"
                class="h-7 w-7 animate-spin"
              />
              <RefreshCw
                v-else-if="statusMeta.icon === 'spinner-alt'"
                class="h-7 w-7 animate-spin"
              />
              <Clock v-else-if="statusMeta.icon === 'clock'" class="h-7 w-7" />
              <CheckCircle v-else-if="statusMeta.icon === 'check'" class="h-7 w-7 text-emerald-600" />
              <XCircle v-else-if="statusMeta.icon === 'x'" class="h-7 w-7 text-red-500" />
              <Trash2 v-else-if="statusMeta.icon === 'trash'" class="h-7 w-7 text-gray-500" />
              <HelpCircle v-else class="h-7 w-7" />
            </div>
            <div>
              <div class="flex items-center gap-2">
                <h1 class="text-xl font-bold text-gray-900">
                  {{ wf.workflowName }}
                </h1>
                <span v-if="isPolling" class="flex h-2 w-2">
                  <span class="absolute inline-flex h-2 w-2 animate-ping rounded-full bg-blue-400 opacity-75" />
                  <span class="relative inline-flex h-2 w-2 rounded-full bg-blue-500" />
                </span>
              </div>
              <div class="mt-0.5 flex items-center gap-2">
                <span class="text-sm text-gray-500">{{ wf.applicationName }} v{{ wf.applicationVersion }}</span>
                <span class="text-gray-300">·</span>
                <span class="text-sm text-gray-500">{{ wf.id }}</span>
              </div>
            </div>
          </div>

          <div class="flex flex-wrap items-center gap-2">
            <AppButton
              v-if="canKill"
              variant="danger"
              size="sm"
              @click="handleKill"
            >
              <XCircle class="mr-1 h-4 w-4" />
              Kill
            </AppButton>
            <AppButton
              v-if="canClean"
              variant="secondary"
              size="sm"
              @click="handleClean"
            >
              <Trash2 class="mr-1 h-4 w-4" />
              Clean
            </AppButton>
            <AppButton
              variant="primary"
              size="sm"
              @click="goToRelaunch"
            >
              <Play class="mr-1 h-4 w-4" />
              Relaunch
            </AppButton>
          </div>
        </div>

        <div class="mt-4 flex flex-wrap gap-4 border-t border-inherit pt-4 text-sm">
          <div>
            <span class="text-gray-500">Status</span>
            <span
              :class="[
                'ml-2 inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-xs font-semibold',
                statusMeta.bg,
              ]"
            >
              <Loader2 v-if="statusMeta.icon === 'spinner'" class="h-3 w-3 animate-spin" />
              {{ statusMeta.label }}
            </span>
          </div>
          <div>
            <span class="text-gray-500">Started</span>
            <span class="ml-2 font-medium text-gray-900">{{ formatRelativeTime(wf.startDate) }}</span>
          </div>
          <div v-if="wf.endDate">
            <span class="text-gray-500">Ended</span>
            <span class="ml-2 font-medium text-gray-900">{{ formatRelativeTime(wf.endDate) }}</span>
          </div>
        </div>
      </div>

      <div class="grid gap-6 md:grid-cols-2">
        <AppCard padding>
          <h2 class="mb-4 text-sm font-semibold uppercase tracking-wider text-gray-500">Details</h2>
          <dl class="space-y-3 text-sm">
            <div class="flex justify-between">
              <dt class="text-gray-500">User</dt>
              <dd class="text-gray-900">{{ wf.userFullName || wf.userId }}</dd>
            </div>
            <div class="flex justify-between">
              <dt class="text-gray-500">Application</dt>
              <dd class="text-gray-900">
                {{ wf.applicationName }}
                <span class="text-gray-400">v{{ wf.applicationVersion }}</span>
              </dd>
            </div>
            <div class="flex justify-between">
              <dt class="text-gray-500">Start date</dt>
              <dd class="text-gray-900">{{ new Date(wf.startDate).toLocaleString() }}</dd>
            </div>
            <div v-if="wf.endDate" class="flex justify-between">
              <dt class="text-gray-500">End date</dt>
              <dd class="text-gray-900">{{ new Date(wf.endDate).toLocaleString() }}</dd>
            </div>
            <div class="flex justify-between">
              <dt class="text-gray-500">Duration</dt>
              <dd class="text-gray-900">
                <template v-if="wf.startDate && wf.endDate">
                  {{ Math.round((new Date(wf.endDate).getTime() - new Date(wf.startDate).getTime()) / 60000) }} min
                </template>
                <span v-else class="text-gray-400">—</span>
              </dd>
            </div>
          </dl>
        </AppCard>

        <AppCard padding>
          <h2 class="mb-4 text-sm font-semibold uppercase tracking-wider text-gray-500">Outputs</h2>
          <div v-if="!wf.outputs || Object.keys(wf.outputs).length === 0" class="text-sm text-gray-500">
            No outputs yet.
          </div>
          <dl v-else class="space-y-3 text-sm">
            <div v-for="(val, key) in wf.outputs" :key="key" class="flex justify-between">
              <dt class="text-gray-500">{{ key }}</dt>
              <dd class="break-all font-mono text-gray-900">{{ val }}</dd>
            </div>
          </dl>
        </AppCard>
      </div>

      <AppCard padding>
        <h2 class="mb-4 text-sm font-semibold uppercase tracking-wider text-gray-500">Inputs</h2>
        <div v-if="!wf.inputs || Object.keys(wf.inputs).length === 0" class="text-sm text-gray-500">
          No inputs.
        </div>
        <div v-else class="overflow-hidden rounded-lg border border-gray-200">
          <table class="min-w-full divide-y divide-gray-200 text-sm">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-2 text-left font-semibold text-gray-700">Parameter</th>
                <th class="px-4 py-2 text-left font-semibold text-gray-700">Value</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
              <tr v-for="(input, name) in wf.inputs" :key="name">
                <td class="px-4 py-2 font-medium text-gray-900">{{ name }}</td>
                <td class="px-4 py-2 font-mono text-xs text-gray-600">
                  <template v-if="input.interval">
                    {{ input.interval.join(' – ') }}
                  </template>
                  <template v-else-if="input.values?.length">
                    {{ input.values.join(', ') }}
                  </template>
                  <span v-else class="text-gray-400">—</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </AppCard>

      <WorkflowLogs :wid="wid" :is-running="isPolling" />

      <JobLogs :wid="wid" :is-running="isPolling" />
    </template>
  </div>
</template>
