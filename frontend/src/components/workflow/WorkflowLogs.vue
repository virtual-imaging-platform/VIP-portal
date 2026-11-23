<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { RefreshCw } from 'lucide-vue-next'
import AppCard from '@/components/ui/AppCard.vue'
import AppButton from '@/components/ui/AppButton.vue'
import LogViewer from '@/components/workflow/LogViewer.vue'
import { workflowsApi } from '@/api/workflows.api'

interface Props {
  wid: string
  isRunning: boolean
}

const props = withDefaults(defineProps<Props>(), {
  isRunning: false,
})

const logContent = ref('')
const logLoading = ref(false)
const activeLogType = ref<'stdout' | 'stderr'>('stdout')

let pollTimer: ReturnType<typeof setInterval> | null = null

const headerLabel = computed(() =>
  activeLogType.value === 'stdout' ? 'Execution Stdout (workflow.out)' : 'Execution Stderr (workflow.err)'
)

const isAutoRefreshActive = computed(() => props.isRunning)

async function fetchLog() {
  logLoading.value = true
  try {
    const content = activeLogType.value === 'stdout'
      ? await workflowsApi.readStdout(props.wid)
      : await workflowsApi.readStderr(props.wid)
    logContent.value = content
  } catch {
    // keep last known content on refresh failure
  } finally {
    logLoading.value = false
  }
}

function switchLogType(type: 'stdout' | 'stderr') {
  if (activeLogType.value === type) return
  activeLogType.value = type
  logContent.value = ''
  fetchLog()
}

function startPolling() {
  stopPolling()
  if (!props.isRunning) return
  pollTimer = setInterval(() => {
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
  activeLogType.value = 'stdout'
  logContent.value = ''
  fetchLog()
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
        <h2 class="text-sm font-semibold uppercase tracking-wider text-gray-500">Execution Logs</h2>

        <div class="flex-1" />

        <span v-if="isAutoRefreshActive" class="flex items-center gap-1 text-xs text-blue-600">
          <RefreshCw class="h-3 w-3 animate-spin" />
          Auto
        </span>
      </div>

      <div class="flex flex-wrap gap-1">
        <AppButton
          variant="ghost"
          size="sm"
          :class="activeLogType === 'stdout' ? 'bg-gray-100 text-gray-900' : ''"
          @click="switchLogType('stdout')"
        >
          Stdout
        </AppButton>
        <AppButton
          variant="ghost"
          size="sm"
          :class="activeLogType === 'stderr' ? 'bg-gray-100 text-gray-900' : ''"
          @click="switchLogType('stderr')"
        >
          Stderr
        </AppButton>
      </div>

      <div class="text-xs font-medium text-gray-700">{{ headerLabel }}</div>

      <LogViewer
        :content="logContent"
        :loading="logLoading"
        :file-name="headerLabel"
      />
    </div>
  </AppCard>
</template>
