<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { Copy, KeyRound, Shield, Trash2, UserRound, Users } from 'lucide-vue-next'

import AppButton from '@/components/ui/AppButton.vue'
import AppCard from '@/components/ui/AppCard.vue'
import AppInput from '@/components/ui/AppInput.vue'
import { apikeyApi } from '@/api/apikey.api'
import { getFrontendBase } from '@/utils/path'
import { sessionApi } from '@/api/session.api'
import { usersApi } from '@/api/users.api'
import { useAuthStore } from '@/stores/auth.store'
import { useGroupsStore } from '@/stores/groups.store'
import { useNotificationsStore } from '@/stores/notifications.store'
import type { Group } from '@/types/group.types'
import type { ProfileUpdatePayload, ProfileUser } from '@/types/profile.types'

const authStore = useAuthStore()
const groupsStore = useGroupsStore()
const notificationsStore = useNotificationsStore()

const isLoading = ref(false)
const isSavingProfile = ref(false)
const isSavingGroups = ref(false)
const isSavingPassword = ref(false)
const isDeleting = ref(false)
const isRegeneratingApiKey = ref(false)
const isDeletingApiKey = ref(false)

const profile = ref<ProfileUser | null>(null)
const apiKey = ref<string | null>(null)
const isLoadingApiKey = ref(false)
const selectedGroupNames = ref<string[]>([])

const profileForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
  institution: '',
  countryCode: '',
})

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const passwordMismatch = computed(() =>
  passwordForm.newPassword.length > 0
  && passwordForm.confirmPassword.length > 0
  && passwordForm.newPassword !== passwordForm.confirmPassword,
)

const hasLoadedProfile = computed(() => profile.value !== null)
const hasSelectableGroups = computed(() => groupsStore.groups.length > 0)
const canSubmitProfile = computed(() => hasLoadedProfile.value)
const canDeleteAccount = computed(() => hasLoadedProfile.value)

const apiKeyPreview = computed(() => {
  if (!apiKey.value) {
    return 'No API key generated yet'
  }
  if (apiKey.value.length <= 14) {
    return apiKey.value
  }
  return `${apiKey.value.slice(0, 8)}...${apiKey.value.slice(-6)}`
})

function hydrateProfileForm(user: ProfileUser) {
  profileForm.firstName = user.firstName ?? ''
  profileForm.lastName = user.lastName ?? ''
  profileForm.email = user.email ?? ''
  profileForm.institution = user.institution ?? ''
  profileForm.countryCode = user.countryCode ?? ''
  selectedGroupNames.value = user.groups?.map((group) => group.name) ?? []
}

function mapSelectedGroups(): Group[] {
  const availableByName = new Map(groupsStore.groups.map((group) => [group.name, group]))
  const existingByName = new Map((profile.value?.groups ?? []).map((group) => [group.name, group]))

  return selectedGroupNames.value
    .map((name) => availableByName.get(name) ?? existingByName.get(name))
    .filter((group): group is Group => Boolean(group))
}

function buildUpdatePayload(): ProfileUpdatePayload | null {
  if (!profile.value) {
    return null
  }

  return {
    id: profile.value.id,
    institution: profileForm.institution.trim(),
    countryCode: profileForm.countryCode.trim().toLowerCase(),
    groups: mapSelectedGroups(),
  }
}

function normalizeProfile(input: unknown): ProfileUser | null {
  if (!input || typeof input !== 'object') {
    return null
  }

  const raw = input as Record<string, unknown>
  const id = typeof raw.id === 'string' ? raw.id : ''
  const email = typeof raw.email === 'string' ? raw.email : ''

  if (!id || !email) {
    return null
  }

  return {
    id,
    firstName: typeof raw.firstName === 'string' ? raw.firstName : '',
    lastName: typeof raw.lastName === 'string' ? raw.lastName : '',
    email,
    institution: typeof raw.institution === 'string' ? raw.institution : '',
    countryCode: typeof raw.countryCode === 'string' ? raw.countryCode : 'fr',
    maxRunningSimulations: typeof raw.maxRunningSimulations === 'number' ? raw.maxRunningSimulations : 1,
    level: typeof raw.level === 'string' ? raw.level : 'Beginner',
    termsOfUse: typeof raw.termsOfUse === 'string' ? raw.termsOfUse : null,
    lastUpdatePublications: typeof raw.lastUpdatePublications === 'string' ? raw.lastUpdatePublications : null,
    welcomeDismissed: typeof raw.welcomeDismissed === 'string' ? raw.welcomeDismissed : null,
    groups: Array.isArray(raw.groups) ? (raw.groups as Group[]) : [],
  }
}

async function fetchCurrentProfile() {
  isLoading.value = true

  try {
    try {
      const fromInternalUser = await sessionApi.getUser()
      const normalized = normalizeProfile(fromInternalUser)
      if (normalized) {
        profile.value = normalized
        hydrateProfileForm(normalized)
        return
      }
    } catch {
      // Fallback to /internal/users/{id} when /internal/user is not exposed.
    }

    try {
      const sessionId = authStore.session?.id
      if (sessionId) {
        const fromUserId = await usersApi.getById(sessionId)
        const normalized = normalizeProfile(fromUserId)
        if (normalized) {
          profile.value = normalized
          hydrateProfileForm(normalized)
          return
        }
      }
    } catch {
      // Keep fallback behavior below.
    }

    profile.value = null
    profileForm.email = authStore.user?.email ?? ''
    notificationsStore.warning('Unable to load full profile data from backend. Profile editing is temporarily limited.')
  } finally {
    isLoading.value = false
  }
}

async function savePersonalInformation() {
  if (!canSubmitProfile.value || !profile.value) {
    notificationsStore.warning('Profile cannot be saved because user metadata is missing.')
    return
  }

  const payload = buildUpdatePayload()
  if (!payload) {
    notificationsStore.error('Invalid profile payload.')
    return
  }

  isSavingProfile.value = true
  try {
    const updated = await usersApi.update(profile.value.id, payload)
    const normalized = normalizeProfile(updated)
    if (normalized) {
      profile.value = normalized
      hydrateProfileForm(normalized)
    }
    notificationsStore.success('Personal information updated successfully.')
  } catch (error: unknown) {
    let message = 'Unable to update personal information.'
    if (axios.isAxiosError(error) && typeof error.response?.data?.message === 'string') {
      message = error.response.data.message
    }
    notificationsStore.error(message)
  } finally {
    isSavingProfile.value = false
  }
}

async function saveGroups() {
  if (!canSubmitProfile.value || !profile.value) {
    notificationsStore.warning('Groups cannot be saved because user metadata is missing.')
    return
  }

  const payload = buildUpdatePayload()
  if (!payload) {
    notificationsStore.error('Invalid groups payload.')
    return
  }

  isSavingGroups.value = true
  try {
    const updated = await usersApi.update(profile.value.id, payload)
    const normalized = normalizeProfile(updated)
    if (normalized) {
      profile.value = normalized
      hydrateProfileForm(normalized)
    }
    notificationsStore.success('Group membership updated.')
  } catch (error: unknown) {
    let message = 'Unable to update groups.'
    if (axios.isAxiosError(error) && typeof error.response?.data?.message === 'string') {
      message = error.response.data.message
    }
    notificationsStore.error(message)
  } finally {
    isSavingGroups.value = false
  }
}

async function submitPasswordChange() {
  if (passwordMismatch.value) {
    notificationsStore.error('The new passwords do not match.')
    return
  }

  if (!profile.value?.email) {
    notificationsStore.error('Unable to update password: missing user email.')
    return
  }

  if (!passwordForm.newPassword.trim()) {
    notificationsStore.error('New password is required.')
    return
  }

  isSavingPassword.value = true
  try {
    await usersApi.updatePassword(profile.value.email, passwordForm.newPassword)
    notificationsStore.success('Your password has been updated.')
    passwordForm.currentPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error: unknown) {
    let message = 'Unable to update password.'
    if (axios.isAxiosError(error) && typeof error.response?.data?.message === 'string') {
      message = error.response.data.message
    }
    notificationsStore.error(message)
  } finally {
    isSavingPassword.value = false
  }
}

function copyApiKey() {
  if (!apiKey.value) {
    notificationsStore.warning('No API key available to copy.')
    return
  }

  navigator.clipboard.writeText(apiKey.value)
    .then(() => notificationsStore.success('API key copied to clipboard.'))
    .catch(() => notificationsStore.error('Unable to copy API key.'))
}

async function fetchApiKey() {
  isLoadingApiKey.value = true
  try {
    apiKey.value = await apikeyApi.get()
  } catch {
    apiKey.value = null
    notificationsStore.warning('Unable to load API key.')
  } finally {
    isLoadingApiKey.value = false
  }
}

async function regenerateApiKey() {
  isRegeneratingApiKey.value = true
  try {
    apiKey.value = await apikeyApi.generateNew()
    notificationsStore.success('New API key generated.')
  } catch {
    notificationsStore.error('Unable to regenerate API key.')
  } finally {
    isRegeneratingApiKey.value = false
  }
}

async function deleteApiKey() {
  isDeletingApiKey.value = true
  try {
    await apikeyApi.delete()
    apiKey.value = null
    notificationsStore.success('API key deleted.')
  } catch {
    notificationsStore.error('Unable to delete API key.')
  } finally {
    isDeletingApiKey.value = false
  }
}

async function deleteAccount() {
  if (!profile.value?.id) {
    notificationsStore.error('Cannot delete account: missing profile identifier.')
    return
  }

  if (!window.confirm('This action is irreversible. Do you really want to delete your account?')) {
    return
  }

  isDeleting.value = true
  try {
    await usersApi.remove(profile.value.id)
    notificationsStore.success('Your account has been deleted.')
    await authStore.logout()
    window.location.assign(`${getFrontendBase()}login`)
  } catch (error: unknown) {
    let message = 'Unable to delete account.'
    if (axios.isAxiosError(error) && typeof error.response?.data?.message === 'string') {
      message = error.response.data.message
    }
    notificationsStore.error(message)
  } finally {
    isDeleting.value = false
  }
}

function toggleGroup(name: string, checked: boolean) {
  if (checked && !selectedGroupNames.value.includes(name)) {
    selectedGroupNames.value = [...selectedGroupNames.value, name]
    return
  }
  if (!checked) {
    selectedGroupNames.value = selectedGroupNames.value.filter((item) => item !== name)
  }
}

onMounted(async () => {
  try {
    await groupsStore.fetchGroups(0, 50)
  } catch {
    notificationsStore.warning('Unable to load available groups.')
  }

  await fetchCurrentProfile()
  await fetchApiKey()
})
</script>

<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-gray-900">My Profile</h1>
      <p class="mt-1 text-sm text-gray-500">
        Manage your account information, security preferences, groups and API access.
      </p>
    </div>

    <div v-if="isLoading" class="py-12 text-sm text-gray-500">Loading your profile...</div>

    <template v-else>
      <AppCard>
        <div class="flex items-center gap-2">
          <UserRound class="h-5 w-5 text-primary-600" />
          <h2 class="text-lg font-semibold text-gray-900">Personal Information</h2>
        </div>

        <div class="mt-4 grid gap-4 md:grid-cols-2">
          <AppInput :model-value="profileForm.firstName" label="First name" disabled />
          <AppInput :model-value="profileForm.lastName" label="Last name" disabled />
          <AppInput :model-value="profileForm.email" label="Email" type="email" disabled />
          <AppInput v-model="profileForm.countryCode" label="Country code" placeholder="fr" />
          <div class="md:col-span-2">
            <AppInput v-model="profileForm.institution" label="Institution" placeholder="Your institution" />
          </div>
        </div>

        <div class="mt-4 flex justify-end">
          <AppButton :loading="isSavingProfile" :disabled="!canSubmitProfile" @click="savePersonalInformation">
            Save personal information
          </AppButton>
        </div>
      </AppCard>

      <div class="grid gap-6 xl:grid-cols-2">
        <AppCard>
          <div class="flex items-center gap-2">
            <Shield class="h-5 w-5 text-primary-600" />
            <h2 class="text-lg font-semibold text-gray-900">Security</h2>
          </div>
          <p class="mt-1 text-sm text-gray-500">Change your account password.</p>

          <div class="mt-4 space-y-4">
            <AppInput
              v-model="passwordForm.currentPassword"
              label="Current password"
              type="password"
              placeholder="Current password"
            />
            <AppInput
              v-model="passwordForm.newPassword"
              label="New password"
              type="password"
              placeholder="New password"
            />
            <AppInput
              v-model="passwordForm.confirmPassword"
              label="Confirm new password"
              type="password"
              placeholder="Confirm password"
              :error="passwordMismatch ? 'Passwords do not match' : undefined"
            />
          </div>

          <div class="mt-4 flex justify-end">
            <AppButton :loading="isSavingPassword" @click="submitPasswordChange">
              Update password
            </AppButton>
          </div>
        </AppCard>

        <AppCard>
          <div class="flex items-center gap-2">
            <Users class="h-5 w-5 text-primary-600" />
            <h2 class="text-lg font-semibold text-gray-900">Groups</h2>
          </div>
          <p class="mt-1 text-sm text-gray-500">Choose the groups associated with your account.</p>

          <div v-if="hasSelectableGroups" class="mt-4 max-h-72 space-y-2 overflow-y-auto pr-1">
            <label
              v-for="group in groupsStore.groups"
              :key="group.name"
              class="flex items-center gap-3 rounded-lg border border-gray-200 px-3 py-2 text-sm text-gray-700"
            >
              <input
                :checked="selectedGroupNames.includes(group.name)"
                type="checkbox"
                class="h-4 w-4 rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                @change="toggleGroup(group.name, ($event.target as HTMLInputElement).checked)"
              />
              <span class="font-medium">{{ group.name }}</span>
              <span class="ml-auto text-xs uppercase tracking-wide text-gray-500">{{ group.type }}</span>
            </label>
          </div>
          <p v-else class="mt-4 text-sm text-gray-500">No groups available.</p>

          <div class="mt-4 flex justify-end">
            <AppButton :loading="isSavingGroups" :disabled="!canSubmitProfile" @click="saveGroups">
              Save groups
            </AppButton>
          </div>
        </AppCard>
      </div>

      <AppCard>
        <div class="flex items-center gap-2">
          <KeyRound class="h-5 w-5 text-primary-600" />
          <h2 class="text-lg font-semibold text-gray-900">API Key</h2>
        </div>
        <p class="mt-1 text-sm text-gray-500">Manage your API key used for external integrations.</p>

        <div class="mt-4 rounded-lg border border-gray-200 bg-gray-50 p-4">
          <p class="text-xs uppercase tracking-wide text-gray-500">Current key</p>
          <p class="mt-1 font-mono text-sm text-gray-800">{{ apiKeyPreview }}</p>
        </div>

        <div class="mt-4 flex flex-wrap gap-2">
          <AppButton variant="secondary" @click="copyApiKey">
            <Copy class="h-4 w-4" />
            Copy
          </AppButton>
          <AppButton :loading="isRegeneratingApiKey" @click="regenerateApiKey">
            Regenerate key
          </AppButton>
          <AppButton variant="danger" :loading="isDeletingApiKey" @click="deleteApiKey">
            Delete key
          </AppButton>
        </div>
      </AppCard>

      <AppCard>
        <div class="rounded-lg border border-red-200 bg-red-50 p-4">
          <div class="flex items-center gap-2">
            <Trash2 class="h-5 w-5 text-red-600" />
            <h2 class="text-lg font-semibold text-red-700">Danger Zone</h2>
          </div>
          <p class="mt-1 text-sm text-red-700">
            Permanently delete your account and all related data. This action cannot be undone.
          </p>

          <div class="mt-4">
            <AppButton variant="danger" :loading="isDeleting" :disabled="!canDeleteAccount" @click="deleteAccount">
              Delete my account
            </AppButton>
          </div>
        </div>
      </AppCard>
    </template>
  </div>
</template>
