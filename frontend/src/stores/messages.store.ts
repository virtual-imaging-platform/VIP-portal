import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { messagesApi, type BackendGroupMessage, type BackendMessage } from '@/api/messages.api'
import { groupsApi } from '@/api/groups.api'
import { useAuthStore } from '@/stores/auth.store'
import { useNotificationsStore } from '@/stores/notifications.store'
import type { MessageItem, SendMessagePayload } from '@/types/message.types'
import type { Group } from '@/types/group.types'

interface GroupThread {
  name: string
  messages: MessageItem[]
  latestDate: string
}

function toDisplayName(user?: { email?: string; firstName?: string; lastName?: string } | null): string {
  if (!user) return 'Unknown'
  const fullName = [user.firstName, user.lastName].filter(Boolean).join(' ')
  return fullName || user.email || 'Unknown'
}

function toDateString(input?: string | number): string {
  if (typeof input === 'number') {
    return new Date(input).toISOString()
  }
  if (typeof input === 'string' && input) {
    return input
  }
  return new Date().toISOString()
}

function mapMessage(message: BackendMessage): MessageItem {
  return {
    id: String(message.id),
    subject: message.title || '(No subject)',
    body: message.message || '',
    from: toDisplayName(message.sender),
    to: (message.receivers || []).map((receiver) => receiver.email || ''),
    date: toDateString(message.postedDate || message.posted),
    read: Boolean(message.read),
    isGroupMessage: false,
  }
}

function mapGroupMessage(message: BackendGroupMessage): MessageItem {
  return {
    id: `g-${message.id}`,
    subject: message.title || '(No subject)',
    body: message.message || '',
    from: toDisplayName(message.sender),
    to: [],
    date: toDateString(message.postedDate || message.posted),
    read: true,
    groupName: message.groupName,
    isGroupMessage: true,
  }
}

function parseRecipients(input: string): string[] {
  return input
    .split(/[;,]/g)
    .map((value) => value.trim())
    .filter(Boolean)
}

export const useMessagesStore = defineStore('messages', () => {
  const messages = ref<MessageItem[]>([])
  const sentMessages = ref<MessageItem[]>([])
  const groupMessages = ref<MessageItem[]>([])
  const availableGroups = ref<Group[]>([])
  const isLoading = ref(false)
  const isLoadingSent = ref(false)
  const isLoadingGroups = ref(false)
  const notifications = useNotificationsStore()
  const authStore = useAuthStore()

  const sortedMessages = computed(() =>
    [...messages.value].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()),
  )

  const sortedSentMessages = computed(() =>
    [...sentMessages.value].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()),
  )

  const unreadCount = computed(() => messages.value.filter((message) => !message.read).length)

  const groupThreads = computed<GroupThread[]>(() => {
    const grouped = groupMessages.value.reduce<Record<string, MessageItem[]>>((acc, message) => {
      const groupName = message.groupName || 'Unknown group'
      acc[groupName] = acc[groupName] || []
      acc[groupName].push(message)
      return acc
    }, {})

    // Ensure all fetched available groups are represented even if they have no messages
    availableGroups.value.forEach((group) => {
      if (!grouped[group.name]) {
        grouped[group.name] = []
      }
    })

    return Object.entries(grouped)
      .map(([name, items]) => {
        const sorted = [...items].sort(
          (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime(),
        )
        return {
          name,
          messages: sorted,
          latestDate: sorted[0]?.date || new Date(0).toISOString(),
        }
      })
      .sort((a, b) => {
        // Sort by latest date first, then by name
        const timeDiff = new Date(b.latestDate).getTime() - new Date(a.latestDate).getTime()
        if (timeDiff !== 0 && b.latestDate !== new Date(0).toISOString() && a.latestDate !== new Date(0).toISOString()) {
          return timeDiff
        }
        if (a.messages.length > 0 && b.messages.length === 0) return -1
        if (b.messages.length > 0 && a.messages.length === 0) return 1
        return a.name.localeCompare(b.name)
      })
  })

  async function fetchMessages(startDate?: number) {
    isLoading.value = true
    try {
      const received = await messagesApi.getReceived(startDate)
      messages.value = received.map(mapMessage)
    } catch {
      notifications.error('Unable to load messages.')
    } finally {
      isLoading.value = false
    }
  }

  async function fetchGroupMessages(startDate?: number): Promise<void> {
    isLoadingGroups.value = true

    if (authStore.user?.level === 'Administrator') {
      try {
        const adminGroupsPage = await groupsApi.getAll()
        availableGroups.value = adminGroupsPage.data
      } catch {
        availableGroups.value = []
      }
    } else {
      availableGroups.value = (authStore.user?.groups ?? []).filter(
        (g) => g.name && authStore.user?.groupsMap?.[g.name],
      )
    }

    if (availableGroups.value.length === 0) {
      groupMessages.value = []
      isLoadingGroups.value = false
      return
    }

    const results = await Promise.allSettled(
      availableGroups.value.map((group) => messagesApi.getGroupMessages(group.name, startDate)),
    )

    let hasFailure = false
    const collected: BackendGroupMessage[] = []

    results.forEach((result) => {
      if (result.status === 'fulfilled') {
        collected.push(...result.value)
      } else {
        hasFailure = true
      }
    })

    if (hasFailure) {
      notifications.error('Unable to load some group messages.')
    }

    groupMessages.value = collected.map(mapGroupMessage)
    isLoadingGroups.value = false
  }

  async function fetchSentMessages(startDate?: number) {
    isLoadingSent.value = true
    try {
      const data = await messagesApi.getSent(startDate)
      sentMessages.value = data.map(mapMessage)
    } catch {
      notifications.error('Unable to load sent messages.')
    } finally {
      isLoadingSent.value = false
    }
  }

  async function sendMessage(payload: SendMessagePayload) {
    if (payload.isGroupMessage) {
      const groupName = parseRecipients(payload.to)[0]
      if (!groupName) {
        notifications.error('Please provide a group name.')
        return
      }
      try {
        await messagesApi.sendGroup(groupName, payload.subject, payload.body)
        notifications.success('Group message sent.')
      } catch {
        notifications.error('Unable to send group message.')
      }
      return
    }

    const recipients = parseRecipients(payload.to)
    if (recipients.length === 0) {
      notifications.error('Please provide at least one recipient.')
      return
    }

    try {
      await messagesApi.send(recipients, payload.subject, payload.body)
      notifications.success('Message sent.')
    } catch {
      notifications.error('Unable to send message.')
    }
  }

  async function deleteMessage(id: string) {
    const numericId = Number(id)
    if (Number.isNaN(numericId)) return

    await messagesApi.deleteReceived(numericId)
    messages.value = messages.value.filter((message) => message.id !== id)
  }

  async function deleteSentMessage(id: string) {
    const numericId = Number(id)
    if (Number.isNaN(numericId)) return

    await messagesApi.deleteSent(numericId)
    sentMessages.value = sentMessages.value.filter((message) => message.id !== id)
  }

  async function deleteGroupMessage(id: string) {
    const numericId = Number(id.replace('g-', ''))
    if (Number.isNaN(numericId)) return

    await messagesApi.deleteGroupMessage(numericId)
    groupMessages.value = groupMessages.value.filter((message) => message.id !== id)
  }

  async function markAsRead(id: string) {
    const message = messages.value.find((item) => item.id === id)
    if (!message || message.isGroupMessage) return

    const numericId = Number(id)
    if (Number.isNaN(numericId)) return

    try {
      await messagesApi.markAsRead(numericId)
      message.read = true
    } catch {
      notifications.error('Unable to mark message as read.')
    }
  }

  return {
    messages,
    sentMessages,
    groupMessages,
    groupThreads,
    sortedMessages,
    sortedSentMessages,
    unreadCount,
    isLoading,
    isLoadingSent,
    isLoadingGroups,
    fetchMessages,
    fetchGroupMessages,
    fetchSentMessages,
    sendMessage,
    deleteMessage,
    deleteSentMessage,
    deleteGroupMessage,
    markAsRead,
  }
})
