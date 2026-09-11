<script setup lang="ts" generic="T extends Record<string, unknown>">
import { computed, ref } from 'vue'

type Key = string | number

const props = defineProps<{
  options: T[]
  modelValue: T[]
  optionValue?: keyof T
  optionLabel?: keyof T
  placeholder?: string
}>()

const optionValue = computed(() => props.optionValue ?? 'id')
const optionLabel = computed(() => props.optionLabel ?? 'label')

const emit = defineEmits<{
  'update:modelValue': [value: T[]]
}>()
const isOpen = ref(false)

const selectedLabels = computed(() =>
  props.modelValue
    .map(option => String(option[optionLabel.value]))
    .join(', '),
)

function isSelected(option: T): boolean {
  return props.modelValue.some(
    selected =>
      selected[optionValue.value] === option[optionValue.value],
  )
}

function toggleOption(option: T) {
  const selected = isSelected(option)

  const updatedValue = selected
    ? props.modelValue.filter(
        item =>
          item[optionValue.value] !== option[optionValue.value],
      )
    : [...props.modelValue, option]

  emit('update:modelValue', updatedValue)
}

function closeDropdown() {
  isOpen.value = false
}
</script>

<template>
  <div class="relative flex flex-col rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60"
      @click.self="closeDropdown">
    <button
      type="button"
      class="rounded-lg border border-gray-300 px-3 py-2 text-sm disabled:opacity-60"
      :aria-expanded="isOpen"
      @click="isOpen = !isOpen"
    >
    <span>{{ selectedLabels || placeholder || 'Select items' }}</span>
    <!-- TODO align right at the border -->
      <span aria-hidden="true">⌄</span>
    </button>

    <div v-if="isOpen" class="flex-col border border-gray-300 px-3 max-h-16 overflow-y-scroll text-sm disabled:opacity-60">
      <label v-for="option in options" :key="String(option[optionValue])">
        <input
            type="checkbox"
            :checked="isSelected(option)"
            @change="toggleOption(option)"
        />
        {{ option[optionLabel] }}
      </label>
    </div>
  </div>
</template>
