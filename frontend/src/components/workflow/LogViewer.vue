<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import { WrapText, AlignLeft, Download } from 'lucide-vue-next'
import AppButton from '@/components/ui/AppButton.vue'

interface Props {
  content: string
  loading?: boolean
  fileName?: string
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  fileName: '',
})

const wrapEnabled = ref(false)
const scrollContainer = ref<HTMLElement | null>(null)

const hasContent = computed(() => props.content.length > 0)

async function scrollToBottom() {
  await nextTick()
  if (scrollContainer.value) {
    scrollContainer.value.scrollTop = scrollContainer.value.scrollHeight
  }
}

function downloadLog() {
  if (!props.content) return
  const blob = new Blob([props.content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = props.fileName ? `${props.fileName}.log` : 'log.txt'
  a.click()
  URL.revokeObjectURL(url)
}

watch(() => props.content, () => {
  if (wrapEnabled.value) scrollToBottom()
})

watch(wrapEnabled, (enabled) => {
  if (enabled) scrollToBottom()
})
</script>

<template>
  <div class="flex flex-col gap-2">
    <div v-if="loading && !hasContent" class="flex items-center gap-2 text-xs text-gray-500">
      <span class="h-3 w-3 animate-spin rounded-full border-2 border-gray-300 border-t-primary-600" />
      Loading…
    </div>

    <div v-if="!loading && !hasContent" class="rounded-lg border border-dashed border-gray-200 bg-gray-50 px-4 py-8 text-center text-sm text-gray-400">
      No log content available.
    </div>

    <template v-if="hasContent">
      <div class="flex items-center gap-1">
        <AppButton variant="ghost" size="sm" @click="wrapEnabled = !wrapEnabled">
          <AlignLeft v-if="wrapEnabled" class="h-3.5 w-3.5" />
          <WrapText v-else class="h-3.5 w-3.5" />
          {{ wrapEnabled ? 'Unwrap' : 'Wrap' }}
        </AppButton>
        <AppButton variant="ghost" size="sm" @click="downloadLog">
          <Download class="h-3.5 w-3.5" />
          Download
        </AppButton>
        <span
          v-if="loading"
          class="flex items-center gap-1 text-xs text-gray-400"
        >
          <span class="h-3 w-3 animate-spin rounded-full border-2 border-gray-300 border-t-gray-500" />
          Refreshing…
        </span>
      </div>

      <pre
        ref="scrollContainer"
        :class="[
          'max-h-96 overflow-auto rounded-lg border border-gray-200 bg-gray-50 p-4 font-mono text-xs leading-relaxed text-gray-800 transition-opacity',
          wrapEnabled ? 'whitespace-pre-wrap break-all' : 'whitespace-pre',
          loading ? 'opacity-50' : 'opacity-100',
        ]"
      >{{ content }}</pre>
    </template>
  </div>
</template>
