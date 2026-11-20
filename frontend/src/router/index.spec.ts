import { beforeEach, describe, expect, it, vi } from 'vitest'

const mocked = vi.hoisted(() => {
  return {
    guard: null as null | ((to: any) => Promise<unknown> | unknown),
    createWebHistory: vi.fn(() => ({ type: 'history' })),
    createRouter: vi.fn((config: any) => {
      return {
        options: config,
        beforeEach: (guard: (to: any) => Promise<unknown> | unknown) => {
          mocked.guard = guard
        },
      }
    }),
    authStore: {
      initialized: true,
      isAuthenticated: false,
      initialize: vi.fn(async () => undefined),
    },
    useAuthStore: vi.fn(() => mocked.authStore),
  }
})

vi.mock('vue-router', () => ({
  createRouter: mocked.createRouter,
  createWebHistory: mocked.createWebHistory,
}))

vi.mock('@/stores/auth.store', () => ({
  useAuthStore: mocked.useAuthStore,
}))

import router from './index'

describe('router', () => {
  beforeEach(() => {
    mocked.useAuthStore.mockClear()
    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = false
    mocked.authStore.initialize = vi.fn(async () => undefined)
  })

  it('registers key routes including optional launch version', () => {
    expect(router).toBeTruthy()

    const routes = (router as any).options?.routes as Array<{
      path: string
      name?: string
      meta?: Record<string, unknown>
    }>
    expect(Array.isArray(routes)).toBe(true)

    const launchRoute = routes.find((route) => route.name === 'application-launch')
    expect(launchRoute?.path).toBe('/applications/:name/launch/:version?')
    expect(launchRoute?.meta?.requiresAuth).toBe(true)

    const loginRoute = routes.find((route) => route.name === 'login')
    expect(loginRoute?.path).toBe('/login')

    const homeRoute = routes.find((route) => route.name === 'home')
    expect(homeRoute?.path).toBe('/')
  })

  it('initializes auth before evaluating access rules', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = false

    const result = await mocked.guard?.({
      name: 'dashboard',
      meta: { requiresAuth: true },
    })

    expect(mocked.authStore.initialize).toHaveBeenCalledTimes(1)
    expect(result).toEqual({ name: 'login' })
  })

  it('redirects authenticated user away from public auth pages', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = true

    const result = await mocked.guard?.({
      name: 'login',
      meta: {},
    })

    expect(result).toEqual({ name: 'dashboard' })
  })

  it('allows protected route when user is authenticated', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = true

    const result = await mocked.guard?.({
      name: 'applications',
      meta: { requiresAuth: true },
    })

    expect(result).toBeUndefined()
    expect(mocked.authStore.initialize).not.toHaveBeenCalled()
  })

  it('keeps landing accessible without authentication', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = false

    const result = await mocked.guard?.({
      name: 'landing',
      meta: {},
    })

    expect(result).toBeUndefined()
  })

  it('redirects root to dashboard only when authenticated', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = true

    const authenticatedResult = await mocked.guard?.({
      name: 'home',
      meta: {},
    })

    expect(authenticatedResult).toEqual({ name: 'dashboard' })

    mocked.authStore.isAuthenticated = false

    const unauthenticatedResult = await mocked.guard?.({
      name: 'home',
      meta: {},
    })

    expect(unauthenticatedResult).toBeUndefined()
  })

  it('redirects unknown routes to landing or dashboard if authenticated', async () => {
    expect(mocked.guard).toBeTypeOf('function')

    mocked.authStore.initialized = true
    mocked.authStore.isAuthenticated = true

    const authenticatedResult = await mocked.guard?.({
      name: 'not-found',
    })

    expect(authenticatedResult).toEqual({ name: 'dashboard' })

    mocked.authStore.isAuthenticated = false

    const unauthenticatedResult = await mocked.guard?.({
      name: 'not-found',
    })

    expect(unauthenticatedResult).toEqual({ name: 'home' })
  })
})
