<script setup lang="ts" generic="TOption, TValue">
import { computed, ref } from 'vue'

const props = defineProps<{
  options: TOption[]
  modelValue: TValue[]
  isSame: (model: TValue, option: TOption) => boolean
  getValue: (option: TOption) => TValue
  getLabel: (option: TOption) => string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: TValue[]]
}>()

const isOpen = ref(false)

const selectedLabels = computed(() =>
  props.options
    .filter(isSelected)
    .map(props.getLabel)
    .join(', '),
)

function isSelected(option: TOption): boolean {
  return props.modelValue.some(selected =>
    props.isSame(selected, option),
  )
}

function toggleOption(option: TOption) {
  const selected = isSelected(option)

  const updated = selected
    ? props.modelValue.filter(item => !props.isSame(item, option))
    : [...props.modelValue, props.getValue(option)]

  emit('update:modelValue', updated)
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
      class="flex w-full rounded-lg text-sm disabled:opacity-60"
      :aria-expanded="isOpen"
      @click="isOpen = !isOpen"
    >
      <span>{{ selectedLabels || 'Select items' }}</span>
      <span aria-hidden="true" class="ml-auto">⌄</span>
    </button>

    <div v-if="isOpen" class="absolute left-0 top-full z-50 mt-2 w-full max-h-48 overflow-y-auto rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm shadow-lg">
      <label v-for="option in options" :key="props.getLabel(option)" class="block">
        <input
            type="checkbox"
            :checked="isSelected(option)"
            @change="toggleOption(option)"
        />
        {{ props.getLabel(option) }}
      </label>
    </div>
  </div>
</template>
