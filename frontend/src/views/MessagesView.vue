<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { Plus, Trash2 } from 'lucide-vue-next'
import AppCard from '@/components/ui/AppCard.vue'
import AppInput from '@/components/ui/AppInput.vue'
import AppButton from '@/components/ui/AppButton.vue'
import AppBadge from '@/components/ui/AppBadge.vue'
import { groupsApi } from '@/api/groups.api'
import { usersApi } from '@/api/users.api'
import { useMessagesStore } from '@/stores/messages.store'
import { useAuthStore } from '@/stores/auth.store'
import { useFormatters } from '@/composables/useFormatters'
import { getGroupBadgeColor } from '@/utils/groupColor'
import { computed } from 'vue'
import type { SendMessagePayload } from '@/types/message.types'
import type { UserSuggestion } from '@/types/user.types'
import type { Group } from '@/types/group.types'

const messagesStore = useMessagesStore()
const authStore = useAuthStore()
const { formatRelativeTime } = useFormatters()

const isAdmin = computed(() => authStore.user?.level === 'Administrator')

const isDeveloper = computed(() => authStore.user?.level === 'Developer')

const canSendUserMessage = computed(() => isAdmin.value || isDeveloper.value)

const canSendGroupMessage = computed(() => {
  if (isAdmin.value) return true
  return Object.values(authStore.user?.groupsMap ?? {}).some((role) => role === 'Admin')
})

const canSendMessage = computed(() => canSendUserMessage.value || canSendGroupMessage.value)

function canDeleteGroupMessage(groupName: string | undefined): boolean {
  if (isAdmin.value) return true
  if (!groupName) return false
  return authStore.user?.groupsMap?.[groupName] === 'Admin'
}

const showComposeModal = ref(false)
const expandedId = ref<string | null>(null)
const sendingMessage = ref(false)
const sendToAll = ref(false)
const activeTab = ref<'received' | 'sent' | 'groups'>('received')
const composeMode = ref<'users' | 'groups'>('users')
const userSuggestions = ref<UserSuggestion[]>([])
const groupSuggestions = ref<Group[]>([])
const isSearchingUsers = ref(false)
const isSearchingGroups = ref(false)
const activeQuery = ref('')
const searchTimer = ref<number | null>(null)
const groupSearchTimer = ref<number | null>(null)
const selectedGroup = ref<string | null>(null)
const expandedGroupMessageId = ref<string | null>(null)

const MIN_QUERY_LENGTH = 2

const recipientMap = new Map<string, string>()

const newMessage = ref<SendMessagePayload & { to: string }>({
  to: '',
  subject: '',
  body: '',
  isGroupMessage: false,
})

function resetForm() {
  recipientMap.clear()
  newMessage.value = {
    to: '',
    subject: '',
    body: '',
    isGroupMessage: false,
  }
  composeMode.value = 'users'
  sendToAll.value = false
  userSuggestions.value = []
  groupSuggestions.value = []
  activeQuery.value = ''
}

async function sendMessage() {
  if (!newMessage.value.to || !newMessage.value.subject) return
  sendingMessage.value = true
  try {
    const rawRecipients = newMessage.value.to.split(/[;,]/).map((s) => s.trim()).filter(Boolean)
    const resolved = rawRecipients.map((r) => recipientMap.get(r) || r).join(', ')
    await messagesStore.sendMessage({
      to: resolved,
      subject: newMessage.value.subject,
      body: newMessage.value.body,
      isGroupMessage: newMessage.value.isGroupMessage,
    })
    showComposeModal.value = false
    resetForm()
  } finally {
    sendingMessage.value = false
  }
}

function openComposeModal() {
  resetForm()
  if (!canSendUserMessage.value && canSendGroupMessage.value) {
    composeMode.value = 'groups'
  }
  showComposeModal.value = true
}

function selectGroup(name: string) {
  selectedGroup.value = name
  expandedGroupMessageId.value = null
}

function toggleGroupMessage(id: string) {
  expandedGroupMessageId.value = expandedGroupMessageId.value === id ? null : id
}

function extractQuery(value: string): string {
  const segments = value.split(/[;,]/)
  return (segments[segments.length - 1] || '').trim()
}

function applySuggestion(user: UserSuggestion) {
  const name = [user.firstName, user.lastName].filter(Boolean).join(' ') || user.id
  recipientMap.set(name, user.id)
  const segments = newMessage.value.to.split(/[;,]/).map((segment) => segment.trim())
  const nextSegments = segments.slice(0, -1).filter((segment) => segment.length > 0)
  nextSegments.push(name)
  newMessage.value.to = `${nextSegments.join(', ')}, `
  userSuggestions.value = []
  activeQuery.value = ''
}

function applyGroupSuggestion(name: string) {
  const segments = newMessage.value.to.split(/[;,]/).map((segment) => segment.trim())
  const nextSegments = segments.slice(0, -1).filter((segment) => segment.length > 0)
  nextSegments.push(name)
  newMessage.value.to = `${nextSegments.join(', ')}, `
  groupSuggestions.value = []
  activeQuery.value = ''
}

function formatSuggestionLabel(user: UserSuggestion): string {
  const fullName = [user.firstName, user.lastName].filter(Boolean).join(' ')
  return fullName || user.id
}

async function onMessageClick(id: string) {
  expandedId.value = expandedId.value === id ? null : id
  const msg = messagesStore.messages.find((m) => m.id === id)
  if (msg && !msg.read) {
    await messagesStore.markAsRead(id)
  }
}

onMounted(() => {
  messagesStore.fetchMessages()
})

watch(activeTab, (tab) => {
  if (tab === 'sent' && messagesStore.sentMessages.length === 0) {
    messagesStore.fetchSentMessages()
  }
  if (tab === 'groups' && messagesStore.groupMessages.length === 0) {
    messagesStore.fetchGroupMessages()
  }
})

watch(
  () => messagesStore.groupThreads,
  (threads) => {
    if (activeTab.value !== 'groups') return
    if (!selectedGroup.value && threads.length > 0) {
      selectedGroup.value = threads[0]?.name ?? null
    }
  },
  { deep: true },
)

watch(
  () => newMessage.value.to,
  (value) => {
    const query = extractQuery(value)
    activeQuery.value = query

    if (searchTimer.value !== null) {
      window.clearTimeout(searchTimer.value)
    }

    if (groupSearchTimer.value !== null) {
      window.clearTimeout(groupSearchTimer.value)
    }

    if (!query || query.length < MIN_QUERY_LENGTH) {
      userSuggestions.value = []
      groupSuggestions.value = []
      isSearchingUsers.value = false
      isSearchingGroups.value = false
      return
    }

    if (composeMode.value === 'users' && !sendToAll.value) {
      searchTimer.value = window.setTimeout(async () => {
        isSearchingUsers.value = true
        try {
          const results = await usersApi.search(query, 10)
          if (activeQuery.value === query) {
            userSuggestions.value = results
          }
        } finally {
          if (activeQuery.value === query) {
            isSearchingUsers.value = false
          }
        }
      }, 250)
    }

    if (composeMode.value === 'groups') {
      groupSearchTimer.value = window.setTimeout(async () => {
        isSearchingGroups.value = true
        try {
          if (isAdmin.value) {
            const results = await groupsApi.search(query, 10)
            if (activeQuery.value === query) {
              groupSuggestions.value = results
            }
          } else {
            // For normal users, only suggest groups where they are an Admin
            if (activeQuery.value === query) {
              const myAdminGroups =
                authStore.user?.groups?.filter((g) => authStore.user?.groupsMap?.[g.name] === 'Admin') ?? []
              groupSuggestions.value = myAdminGroups.filter((g) =>
                g.name.toLowerCase().includes(query.toLowerCase()),
              )
            }
          }
        } catch {
          if (activeQuery.value === query) {
            groupSuggestions.value = []
          }
        } finally {
          if (activeQuery.value === query) {
            isSearchingGroups.value = false
          }
        }
      }, 250)
    }
  },
)

watch(sendToAll, (enabled) => {
  if (enabled) {
    newMessage.value.to = 'All'
    userSuggestions.value = []
    activeQuery.value = ''
    return
  }

  if (newMessage.value.to.trim().toLowerCase() === 'all') {
    newMessage.value.to = ''
  }
})

watch(composeMode, (mode) => {
  if (mode === 'groups') {
    sendToAll.value = false
    userSuggestions.value = []
    activeQuery.value = ''
    newMessage.value.isGroupMessage = true
    return
  }

  newMessage.value.isGroupMessage = false
})
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <h1 class="flex items-center gap-2 text-2xl font-bold text-gray-900">
        Messages
        <AppBadge v-if="messagesStore.unreadCount" variant="primary">
          {{ messagesStore.unreadCount }}
        </AppBadge>
      </h1>
      <AppButton v-if="canSendMessage" @click="openComposeModal">
        <Plus class="h-4 w-4" />
        New message
      </AppButton>
    </div>

    <div class="flex flex-wrap gap-2">
      <AppButton
        type="button"
        :variant="activeTab === 'received' ? 'primary' : 'secondary'"
        @click="activeTab = 'received'"
      >
        Inbox
      </AppButton>
      <AppButton
        type="button"
        :variant="activeTab === 'sent' ? 'primary' : 'secondary'"
        @click="activeTab = 'sent'"
      >
        Sent
      </AppButton>
      <AppButton
        type="button"
        :variant="activeTab === 'groups' ? 'primary' : 'secondary'"
        @click="activeTab = 'groups'"
      >
        Groups
      </AppButton>
    </div>

    <div v-if="activeTab === 'received'" class="space-y-2">
      <AppCard
        v-for="msg in messagesStore.sortedMessages"
        :key="msg.id"
        padding
        hoverable
        class="cursor-pointer"
        @click="onMessageClick(msg.id)"
      >
        <div class="flex items-start gap-3">
          <span
            v-if="!msg.read"
            class="mt-1.5 h-2 w-2 shrink-0 rounded-full bg-primary-600"
          />
          <span v-else class="w-2 shrink-0" />
          <div class="min-w-0 flex-1">
            <div class="flex flex-wrap items-start justify-between gap-2">
              <div class="flex flex-wrap items-center gap-2">
                <p
                  :class="[
                    'font-medium',
                    msg.read ? 'text-gray-700' : 'font-bold text-gray-900',
                  ]"
                >
                  {{ msg.subject }}
                </p>
                <AppBadge
                  v-if="msg.groupName"
                  :variant="getGroupBadgeColor(msg.groupName)"
                >
                  {{ msg.groupName }}
                </AppBadge>
              </div>
              <AppButton
                variant="ghost"
                size="sm"
                @click.stop="messagesStore.deleteMessage(msg.id)"
              >
                <Trash2 class="h-4 w-4 text-red-600" />
              </AppButton>
            </div>
            <p class="text-sm text-gray-500">
              {{ msg.from }} · {{ formatRelativeTime(msg.date) }}
            </p>
            <p
              v-if="expandedId === msg.id"
              class="mt-2 whitespace-pre-wrap text-sm text-gray-600"
            >
              {{ msg.body }}
            </p>
            <p
              v-else
              class="mt-1 line-clamp-2 text-sm text-gray-500"
            >
              {{ msg.body.slice(0, 120) }}{{ msg.body.length > 120 ? '...' : '' }}
            </p>
          </div>
        </div>
      </AppCard>
    </div>

    <div v-else-if="activeTab === 'sent'" class="space-y-2">
      <AppCard
        v-for="msg in messagesStore.sortedSentMessages"
        :key="msg.id"
        padding
        hoverable
        class="cursor-pointer"
        @click="expandedId = expandedId === msg.id ? null : msg.id"
      >
        <div class="flex items-start gap-3">
          <span class="w-2 shrink-0" />
          <div class="min-w-0 flex-1">
            <div class="flex flex-wrap items-start justify-between gap-2">
              <p class="font-medium text-gray-900">
                {{ msg.subject }}
              </p>
              <AppButton
                variant="ghost"
                size="sm"
                @click.stop="messagesStore.deleteSentMessage(msg.id)"
              >
                <Trash2 class="h-4 w-4 text-red-600" />
              </AppButton>
            </div>
            <p class="text-sm text-gray-500">
              To {{ msg.to.join(', ') || '-' }} · {{ formatRelativeTime(msg.date) }}
            </p>
            <p
              v-if="expandedId === msg.id"
              class="mt-2 whitespace-pre-wrap text-sm text-gray-600"
            >
              {{ msg.body }}
            </p>
            <p
              v-else
              class="mt-1 line-clamp-2 text-sm text-gray-500"
            >
              {{ msg.body.slice(0, 120) }}{{ msg.body.length > 120 ? '...' : '' }}
            </p>
          </div>
        </div>
      </AppCard>
    </div>

    <div v-else class="flex flex-col gap-4 lg:flex-row">
      <aside class="w-full max-w-xs space-y-2 lg:w-1/4">
        <AppCard
          v-for="group in messagesStore.groupThreads"
          :key="group.name"
          padding
          hoverable
          :class="[
            'cursor-pointer transition',
            selectedGroup === group.name ? 'border-primary-500 bg-primary-50' : 'border-transparent',
          ]"
          @click="selectGroup(group.name)"
        >
          <div class="flex flex-wrap items-center justify-between gap-2">
            <div class="flex flex-wrap items-center gap-2">
              <AppBadge :variant="getGroupBadgeColor(group.name)">
                {{ group.name }}
              </AppBadge>
              <p class="text-xs text-gray-500">
                {{ group.messages.length }} messages
              </p>
            </div>
            <p class="text-xs text-gray-500">
              {{ formatRelativeTime(group.latestDate) }}
            </p>
          </div>
        </AppCard>
      </aside>

      <section class="flex-1 space-y-2">
        <template v-if="selectedGroup">
          <AppCard
            v-for="msg in messagesStore.groupThreads.find((g) => g.name === selectedGroup)?.messages || []"
            :key="msg.id"
            padding
            hoverable
            class="cursor-pointer"
            @click="toggleGroupMessage(msg.id)"
          >
            <div class="flex flex-wrap items-start justify-between gap-2">
              <p class="font-medium text-gray-900">
                {{ msg.subject }}
              </p>
              <div class="flex items-center gap-2">
                <AppButton
                  v-if="canDeleteGroupMessage(msg.groupName)"
                  variant="ghost"
                  size="sm"
                  @click.stop="messagesStore.deleteGroupMessage(msg.id)"
                >
                  <Trash2 class="h-4 w-4 text-red-600" />
                </AppButton>
                <p class="text-xs text-gray-500">
                  {{ formatRelativeTime(msg.date) }}
                </p>
              </div>
            </div>
            <p class="text-sm text-gray-500">
              {{ msg.from }}
            </p>
            <p
              v-if="expandedGroupMessageId === msg.id"
              class="mt-2 whitespace-pre-wrap text-sm text-gray-600"
            >
              {{ msg.body }}
            </p>
            <p
              v-else
              class="mt-1 line-clamp-2 text-sm text-gray-500"
            >
              {{ msg.body.slice(0, 160) }}{{ msg.body.length > 160 ? '...' : '' }}
            </p>
          </AppCard>
        </template>
        <p v-else class="py-12 text-center text-sm text-gray-500">
          Sélectionnez un groupe pour afficher ses messages.
        </p>
      </section>
    </div>

    <p
      v-if="activeTab === 'received' && !messagesStore.isLoading && messagesStore.sortedMessages.length === 0"
      class="py-12 text-center text-gray-500"
    >
      No messages
    </p>
    <p
      v-if="activeTab === 'sent' && !messagesStore.isLoadingSent && messagesStore.sortedSentMessages.length === 0"
      class="py-12 text-center text-gray-500"
    >
      No sent messages
    </p>
    <p
      v-if="activeTab === 'groups' && !messagesStore.isLoadingGroups && messagesStore.groupThreads.length === 0"
      class="py-12 text-center text-gray-500"
    >
      No group messages
    </p>

    <div
      v-if="showComposeModal"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4"
      @click.self="showComposeModal = false"
    >
      <div class="w-full max-w-2xl rounded-xl border border-gray-200 bg-white p-6 shadow-lg">
        <div class="flex items-center justify-between gap-4">
          <h2 class="text-lg font-semibold text-gray-900">New message</h2>
          <button
            type="button"
            class="rounded-lg p-1.5 text-gray-400 hover:bg-gray-100 hover:text-gray-600"
            @click="showComposeModal = false"
            aria-label="Close"
          >
            ×
          </button>
        </div>
        <form class="mt-4 space-y-4" @submit.prevent="sendMessage">
          <div v-if="canSendUserMessage && canSendGroupMessage" class="flex flex-wrap gap-2">
            <AppButton
              type="button"
              :variant="composeMode === 'users' ? 'primary' : 'secondary'"
              @click="composeMode = 'users'"
            >
              Message users
            </AppButton>
            <AppButton
              type="button"
              :variant="composeMode === 'groups' ? 'primary' : 'secondary'"
              @click="composeMode = 'groups'"
            >
              Message groups
            </AppButton>
          </div>
          <AppInput
            v-model="newMessage.to"
            :label="composeMode === 'users' ? 'Recipients' : 'Groups'"
            required
            :disabled="sendToAll && composeMode === 'users'"
          />
          <label v-if="composeMode === 'users'" class="flex items-center gap-2 text-sm text-gray-700">
            <input
              v-model="sendToAll"
              type="checkbox"
              class="h-4 w-4 rounded border-gray-300 text-primary-600 focus:ring-primary-500"
            />
            Send to all users
          </label>
          <div
            v-if="composeMode === 'users' && activeQuery && (isSearchingUsers || userSuggestions.length > 0)"
            class="rounded-lg border border-gray-200 bg-white p-2 shadow-sm"
          >
            <p class="px-2 pb-1 text-xs font-semibold uppercase tracking-wide text-gray-500">
              Suggestions
            </p>
            <div class="space-y-1">
              <p v-if="isSearchingUsers" class="px-2 text-sm text-gray-500">Searching...</p>
              <button
                v-for="user in userSuggestions"
                :key="user.id"
                type="button"
                class="flex w-full items-center justify-between rounded-md px-2 py-1 text-left text-sm text-gray-700 hover:bg-gray-50"
                @click="applySuggestion(user)"
              >
                <span>{{ formatSuggestionLabel(user) }}</span>
              </button>
              <p
                v-if="!isSearchingUsers && userSuggestions.length === 0"
                class="px-2 text-sm text-gray-500"
              >
                No suggestions
              </p>
            </div>
          </div>
          <div
            v-if="composeMode === 'groups' && activeQuery && (isSearchingGroups || groupSuggestions.length > 0)"
            class="rounded-lg border border-gray-200 bg-white p-2 shadow-sm"
          >
            <p class="px-2 pb-1 text-xs font-semibold uppercase tracking-wide text-gray-500">
              Group suggestions
            </p>
            <div class="space-y-1">
              <p v-if="isSearchingGroups" class="px-2 text-sm text-gray-500">Searching...</p>
              <button
                v-for="group in groupSuggestions"
                :key="group.name"
                type="button"
                class="flex w-full items-center justify-between rounded-md px-2 py-1 text-left text-sm text-gray-700 hover:bg-gray-50"
                @click="applyGroupSuggestion(group.name)"
              >
                <span>{{ group.name }}</span>
              </button>
              <p
                v-if="!isSearchingGroups && groupSuggestions.length === 0"
                class="px-2 text-sm text-gray-500"
              >
                No suggestions
              </p>
            </div>
          </div>
          <AppInput
            v-model="newMessage.subject"
            label="Subject"
            required
          />
          <div>
            <label class="mb-1 block text-sm font-medium text-gray-700">
              Body
            </label>
            <textarea
              v-model="newMessage.body"
              rows="5"
              class="block w-full rounded-lg border border-gray-300 px-3 py-2 text-sm placeholder:text-gray-400 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:ring-offset-0 disabled:bg-gray-50 disabled:text-gray-500"
              placeholder="Your message..."
            />
          </div>
          <p class="text-xs text-gray-500">
            Multi-recipient is supported with commas.
          </p>
          <div class="flex justify-end gap-2 pt-2">
            <AppButton type="button" variant="secondary" @click="showComposeModal = false">
              Cancel
            </AppButton>
            <AppButton type="submit" :loading="sendingMessage">
              Send
            </AppButton>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
