import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { sessionApi } from '@/api/session.api'
import { usersApi } from '@/api/users.api'
import type { VipSession, LoginCredentials, RegisterPayload } from '@/types/auth.types'
import type { ProfileUser } from '@/types/profile.types'



export const useAuthStore = defineStore('auth', () => {
  const user = ref<ProfileUser | null>(null)
  const session = ref<VipSession | null>(null)
  const isLoading = ref(false)
  const initialized = ref(false)

  const isAuthenticated = computed(() => !!session.value)

  async function initialize() {
    if (initialized.value) return

    try {
      const vipSession = await sessionApi.getSession()
      if (vipSession.confirmed === false) {
        session.value = null
        user.value = null
      } else {
        session.value = vipSession
        await loadCurrentUser()
      }
    } catch {
      session.value = null
      user.value = null
    } finally {
      initialized.value = true
    }
  }

  async function login(credentials: LoginCredentials) {
    isLoading.value = true;
    try {
      const vipSession = await sessionApi.login(credentials);
      if (vipSession.confirmed === false) {
        return vipSession;
      }
      session.value = vipSession;
      await loadCurrentUser();
      return vipSession;
    } finally {
      isLoading.value = false;
    }
  }

  async function loadCurrentUser() {
    try {
      user.value = await usersApi.me()
    } catch {
      user.value = null
    }
  }

  async function logout() {
    try {
      await sessionApi.logout()
    } catch {
    }
    user.value = null
    session.value = null
    initialized.value = false
  }

  async function register(payload: RegisterPayload) {
    isLoading.value = true
    try {
      await usersApi.register(payload)
    } finally {
      isLoading.value = false
    }
  }

  async function activate(email: string, code: string) {
    isLoading.value = true
    try {
      await usersApi.activate(email, code)
      // Assuming activation logs the user in (sets cookies), check session:
      try {
        const vipSession = await sessionApi.getSession()
        session.value = vipSession
        await loadCurrentUser()
        console.log('[auth.activate] Session OK, user:', user.value?.email)
      } catch (e) {
        console.warn('[auth.activate] Failed to get session after activation', e)
      }
    } finally {
      isLoading.value = false
    }
  }

  return {
    user,
    session,
    isLoading,
    initialized,
    isAuthenticated,
    initialize,
    login,
    register,
    activate,
    logout,
  }
})
