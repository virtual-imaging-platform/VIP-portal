<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

import {
  LayoutDashboard,
  AppWindow,
  Play,
  FolderOpen,
  BookOpen,
  Mail,
  ServerCog,
  User,
  Shield,
  Users,
  Layers,
  X,
} from 'lucide-vue-next'

interface Props {
  open?: boolean
}

withDefaults(defineProps<Props>(), {
  open: false,
})

const emit = defineEmits<{
  close: []
}>()

const route = useRoute()
const auth = useAuthStore()
const isAdmin = computed(() => auth.user?.level === 'Administrator')

interface NavItem {
  name: string
  to: string
  icon: typeof LayoutDashboard
  badge?: number
  adminOnly?: boolean
}

const mainNav = computed<NavItem[]>(() => [
  { name: 'Dashboard', to: '/dashboard', icon: LayoutDashboard },
  { name: 'Applications', to: '/applications', icon: AppWindow },
  {
    name: 'Executions',
    to: '/executions',
    icon: Play,
  },
  { name: 'Files', to: '/files', icon: FolderOpen },
  { name: 'Publications', to: '/publications', icon: BookOpen },
  {
    name: 'Messages',
    to: '/messages',
    icon: Mail,
  }
])

const adminNav = computed<NavItem[]>(() => [
  {
    name: 'Engines',
    to: '/engines',
    icon: ServerCog,
  }
])

const bottomNav = computed<NavItem[]>(() => [
  { name: 'My account', to: '/profile', icon: User },
])

function isActive(path: string) {
  return route.path === path || route.path.startsWith(path + '/')
}
</script>

<template>
  <!-- Sidebar desktop -->
  <aside
    class="fixed left-0 top-0 z-30 hidden h-full w-64 flex-col border-r border-gray-200 bg-white lg:flex"
  >
    <div class="flex h-16 items-center gap-3 border-b border-gray-100 px-6">
      <!-- logo in @assets/logo.png -->
      <img
        src="@/assets/vip-logo-without-text.png"
        alt="VIP Logo"
        class="h-8 w-auto rounded-sm object-cover"
      />
      <div>
        <p class="text-sm font-semibold text-gray-900">VIP Portal</p>
        <p class="text-xs text-gray-500">Virtual Imaging Platform</p>
      </div>
    </div>

    <nav class="flex-1 space-y-1 overflow-y-auto px-3 py-4">
      <RouterLink
        v-for="item in mainNav"
        :key="item.to"
        :to="item.to"
        :class="[
          'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
          isActive(item.to)
            ? 'bg-primary-50 text-primary-700'
            : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
        ]"
      >
        <component
          :is="item.icon"
          :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
        />
        <span class="flex-1">{{ item.name }}</span>
        <span
          v-if="item.badge"
          class="flex h-5 min-w-5 items-center justify-center rounded-full bg-primary-600 px-1.5 text-xs font-medium text-white"
        >
          {{ item.badge }}
        </span>
      </RouterLink>
      <template v-if="isAdmin">
        <div class="divider border-b border-gray-300" role="separator" aria-hidden="true"></div>
        <RouterLink
          v-for="item in adminNav"
          :key="item.to"
          :to="item.to"
          :class="[
            'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
            isActive(item.to)
              ? 'bg-primary-50 text-primary-700'
              : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
          ]"
        >
          <component
            :is="item.icon"
            :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
          />
          <span class="flex-1">{{ item.name }}</span>
          <span
            v-if="item.badge"
            class="flex h-5 min-w-5 items-center justify-center rounded-full bg-primary-600 px-1.5 text-xs font-medium text-white"
          >
            {{ item.badge }}
          </span>
        </RouterLink>
      </template>
    </nav>

    <div class="border-t border-gray-100 px-3 py-4 space-y-1">
      <RouterLink
        v-for="item in bottomNav"
        :key="item.to"
        :to="item.to"
        :class="[
          'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
          isActive(item.to)
            ? 'bg-primary-50 text-primary-700'
            : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
        ]"
      >
        <component
          :is="item.icon"
          :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
        />
        {{ item.name }}
      </RouterLink>
    </div>
  </aside>

  <!-- Sidebar mobile -->
  <Teleport to="body">
    <Transition name="sidebar">
      <div v-if="open" class="fixed inset-0 z-40 flex lg:hidden">
        <div class="absolute inset-0 bg-black/40" @click="emit('close')" />
        <aside
          class="relative flex h-full w-72 flex-col border-r border-gray-200 bg-white shadow-xl"
        >
          <div class="flex h-16 items-center justify-between gap-3 border-b border-gray-100 px-4">
            <div class="flex items-center gap-3">
              <!-- logo -->
              <div>
                <p class="text-sm font-semibold text-gray-900">VIP Portal</p>
                <p class="text-xs text-gray-500">Virtual Imaging Platform</p>
              </div>
            </div>
            <button
              type="button"
              class="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-gray-600"
              @click="emit('close')"
            >
              <X class="h-5 w-5" />
            </button>
          </div>

          <nav class="flex-1 space-y-1 overflow-y-auto px-3 py-4">
            <RouterLink
              v-for="item in mainNav"
              :key="item.to"
              :to="item.to"
              :class="[
                'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
                isActive(item.to)
                  ? 'bg-primary-50 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
              ]"
              @click="emit('close')"
            >
              <component
                :is="item.icon"
                :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
              />
              <span class="flex-1">{{ item.name }}</span>
              <span
                v-if="item.badge"
                class="flex h-5 min-w-5 items-center justify-center rounded-full bg-primary-600 px-1.5 text-xs font-medium text-white"
              >
                {{ item.badge }}
              </span>
            </RouterLink>
            <RouterLink
              v-for="item in adminNav"
              :key="item.to"
              :to="item.to"
              :class="[
                'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
                isActive(item.to)
                  ? 'bg-primary-50 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
              ]"
              @click="emit('close')"
            >
              <component
                :is="item.icon"
                :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
              />
              <span class="flex-1">{{ item.name }}</span>
              <span
                v-if="item.badge"
                class="flex h-5 min-w-5 items-center justify-center rounded-full bg-primary-600 px-1.5 text-xs font-medium text-white"
              >
                {{ item.badge }}
              </span>
            </RouterLink>
          </nav>

          <div class="border-t border-gray-100 px-3 py-4 space-y-1">
            <RouterLink
              v-for="item in bottomNav"
              :key="item.to"
              :to="item.to"
              :class="[
                'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-150',
                isActive(item.to)
                  ? 'bg-primary-50 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
              ]"
              @click="emit('close')"
            >
              <component
                :is="item.icon"
                :class="['h-5 w-5 shrink-0', isActive(item.to) ? 'text-primary-600' : 'text-gray-400']"
              />
              {{ item.name }}
            </RouterLink>
          </div>
        </aside>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.sidebar-enter-active,
.sidebar-leave-active {
  transition: opacity 0.15s ease, transform 0.15s ease;
}
.sidebar-enter-from,
.sidebar-leave-to {
  opacity: 0;
  transform: translateX(-8px);
}
</style>
