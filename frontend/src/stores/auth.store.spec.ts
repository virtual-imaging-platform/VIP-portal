import { beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from './auth.store'

const mocked = vi.hoisted(() => ({
  getSession: vi.fn(),
  login: vi.fn(),
  verifyCode: vi.fn(),
  resendCode: vi.fn(),
  logout: vi.fn(),
  register: vi.fn(),
  me: vi.fn(),
}))

vi.mock('@/api/session.api', () => ({
  sessionApi: {
    getSession: mocked.getSession,
    login: mocked.login,
    verifyCode: mocked.verifyCode,
    resendCode: mocked.resendCode,
    logout: mocked.logout,
  },
}))

vi.mock('@/api/users.api', () => ({
  usersApi: {
    register: mocked.register,
    me: mocked.me,
  },
}))

describe('useAuthStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('initializes from existing session and sets user', async () => {
    mocked.getSession.mockResolvedValue({
      id: 'session-1',
      email: 'user@example.com',
      userlevel: 'User',
    })
    mocked.me.mockResolvedValue({
      id: 'user-1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'user@example.com',
      institution: 'VIP Lab',
      countryCode: 'fr',
      maxRunningSimulations: 3,
      level: 'User',
      termsOfUse: null,
      lastUpdatePublications: null,
      groups: [],
      apiKey: null,
    })

    const store = useAuthStore()
    await store.initialize()

    expect(mocked.getSession).toHaveBeenCalledTimes(1)
    expect(store.initialized).toBe(true)
    expect(store.isAuthenticated).toBe(true)
    expect(store.user).toEqual(expect.objectContaining({ email: 'user@example.com' }))

    await store.initialize()
    expect(mocked.getSession).toHaveBeenCalledTimes(1)
  })

  it('handles initialize failure and still marks as initialized', async () => {
    mocked.getSession.mockRejectedValue(new Error('network'))

    const store = useAuthStore()
    await store.initialize()

    expect(store.initialized).toBe(true)
    expect(store.isAuthenticated).toBe(false)
    expect(store.user).toBeNull()
    expect(store.session).toBeNull()
  })

  it('logs in and waits for the verification code', async () => {
    mocked.login.mockResolvedValue({ twoFactorRequired: true })

    const store = useAuthStore()
    await store.login({ username: 'login@example.com', password: 'secret' })

    expect(mocked.login).toHaveBeenCalledWith({ username: 'login@example.com', password: 'secret' })
    expect(store.isLoading).toBe(false)
    expect(store.twoFactorPending).toBe(true)
    expect(store.isAuthenticated).toBe(false)
    expect(mocked.me).not.toHaveBeenCalled()
  })

  it('verifies the code and updates auth state', async () => {
    mocked.verifyCode.mockResolvedValue({
      id: 'session-2',
      email: 'login@example.com',
      userlevel: 'User',
      confirmed: true,
    })
    mocked.me.mockResolvedValue({
      id: 'user-2',
      firstName: 'Login',
      lastName: 'User',
      email: 'login@example.com',
      institution: 'VIP Lab',
      countryCode: 'fr',
      maxRunningSimulations: 3,
      level: 'User',
      termsOfUse: null,
      lastUpdatePublications: null,
      groups: [],
      apiKey: null,
    })

    const store = useAuthStore()
    store.twoFactorPending = true
    await store.verifyCode('123456')

    expect(mocked.verifyCode).toHaveBeenCalledWith('123456')
    expect(store.isLoading).toBe(false)
    expect(store.twoFactorPending).toBe(false)
    expect(store.isAuthenticated).toBe(true)
    expect(store.user).toEqual(expect.objectContaining({ email: 'login@example.com' }))
  })

  it('propagates login error and resets loading flag', async () => {
    mocked.login.mockRejectedValue(new Error('invalid credentials'))

    const store = useAuthStore()

    await expect(store.login({ username: 'bad', password: 'bad' })).rejects.toThrow('invalid credentials')
    expect(store.isLoading).toBe(false)
    expect(store.isAuthenticated).toBe(false)
  })

  it('clears auth state on logout even if API fails', async () => {
    const store = useAuthStore()
    store.session = { id: 'session-3', email: 'x@y.z', userlevel: 'User' } as any
    store.user = { email: 'x@y.z' } as any
    store.initialized = true
    store.twoFactorPending = true

    mocked.logout.mockRejectedValue(new Error('logout failed'))

    await store.logout()

    expect(mocked.logout).toHaveBeenCalledTimes(1)
    expect(store.session).toBeNull()
    expect(store.user).toBeNull()
    expect(store.initialized).toBe(false)
    expect(store.twoFactorPending).toBe(false)
  })

  it('registers user through /internal/users and resets loading state', async () => {
    mocked.register.mockResolvedValue(undefined)
    const store = useAuthStore()

    await store.register({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'secret',
      countryCode: 'FR',
      institution: 'VIP Lab',
      comments: 'test',
    })

    expect(mocked.register).toHaveBeenCalledWith({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      password: 'secret',
      countryCode: 'FR',
      institution: 'VIP Lab',
      comments: 'test',
    })
    expect(store.isLoading).toBe(false)
  })
})
