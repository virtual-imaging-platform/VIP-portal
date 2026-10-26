import { beforeEach, describe, expect, it, vi } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import LoginView from './LoginView.vue'

const mocked = vi.hoisted(() => ({
  push: vi.fn(),
  login: vi.fn(),
  notifySuccess: vi.fn(),
  notifyError: vi.fn(),
  isAxiosError: vi.fn((value: unknown) => Boolean((value as { isAxiosError?: boolean })?.isAxiosError)),
  verifyCode: vi.fn(),
  resendCode: vi.fn(),
  cancelTwoFactor: vi.fn(),
  authStore: {
    isLoading: false,
    twoFactorPending: false,
    login: vi.fn(),
    verifyCode: vi.fn(),
    resendCode: vi.fn(),
    cancelTwoFactor: vi.fn(),
  },
}))

vi.mock('vue-router', () => ({
  useRouter: () => ({ push: mocked.push }),
  RouterLink: {
    name: 'RouterLink',
    props: ['to'],
    template: '<a><slot /></a>',
  },
}))

vi.mock('@/stores/auth.store', () => ({
  useAuthStore: () => mocked.authStore,
}))

vi.mock('@/stores/notifications.store', () => ({
  useNotificationsStore: () => ({
    success: mocked.notifySuccess,
    error: mocked.notifyError,
  }),
}))

vi.mock('axios', () => ({
  default: {
    isAxiosError: mocked.isAxiosError,
  },
}))

describe('LoginView', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mocked.authStore.login = mocked.login
    mocked.authStore.verifyCode = mocked.verifyCode
    mocked.authStore.resendCode = mocked.resendCode
    mocked.authStore.cancelTwoFactor = mocked.cancelTwoFactor
    mocked.authStore.isLoading = false
    mocked.authStore.twoFactorPending = false
  })

  it('sends the password and asks for the verification code', async () => {
    mocked.login.mockResolvedValue({ twoFactorRequired: true })

    const wrapper = mount(LoginView)

    await wrapper.get('input[type="email"]').setValue('user@example.com')
    await wrapper.get('input[type="password"]').setValue('secret')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(mocked.login).toHaveBeenCalledWith({ username: 'user@example.com', password: 'secret' })
    expect(mocked.notifySuccess).toHaveBeenCalledWith('A verification code has been sent to your email.')
    expect(mocked.push).not.toHaveBeenCalled()
  })

  it('shows dedicated error message on 401', async () => {
    mocked.login.mockRejectedValue({
      isAxiosError: true,
      response: { status: 401 },
    })

    const wrapper = mount(LoginView)

    await wrapper.get('input[type="email"]').setValue('user@example.com')
    await wrapper.get('input[type="password"]').setValue('wrong')
    await wrapper.get('form').trigger('submit')

    expect(mocked.notifyError).toHaveBeenCalledWith('Invalid email or password.')
    expect(mocked.push).not.toHaveBeenCalled()
  })

  it('verifies the code and redirects to dashboard', async () => {
    mocked.authStore.twoFactorPending = true
    mocked.verifyCode.mockResolvedValue({ confirmed: true })

    const wrapper = mount(LoginView)

    await wrapper.get('input[type="text"]').setValue('123456')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(mocked.verifyCode).toHaveBeenCalledWith('123456')
    expect(mocked.notifySuccess).toHaveBeenCalledWith('Vous êtes maintenant connecté.')
    expect(mocked.push).toHaveBeenCalledWith('/dashboard')
  })

  it('redirects to activation when the account is not confirmed', async () => {
    mocked.authStore.twoFactorPending = true
    mocked.verifyCode.mockResolvedValue({ confirmed: false })

    const wrapper = mount(LoginView)

    await wrapper.get('input[type="text"]').setValue('123456')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(mocked.push).toHaveBeenCalledWith(expect.objectContaining({ name: 'activate' }))
  })

  it('shows an error message on wrong code', async () => {
    mocked.authStore.twoFactorPending = true
    mocked.verifyCode.mockRejectedValue({
      isAxiosError: true,
      response: { status: 401 },
    })

    const wrapper = mount(LoginView)

    await wrapper.get('input[type="text"]').setValue('000000')
    await wrapper.get('form').trigger('submit')
    await flushPromises()

    expect(mocked.notifyError).toHaveBeenCalledWith(
      'Invalid or expired code. If it keeps failing, go back and log in again.',
    )
    expect(mocked.push).not.toHaveBeenCalled()
  })

  it('resends the code', async () => {
    mocked.authStore.twoFactorPending = true
    mocked.resendCode.mockResolvedValue(undefined)

    const wrapper = mount(LoginView)

    const resendButton = wrapper.findAll('button').find((b) => b.text() === 'Resend code')
    await resendButton!.trigger('click')
    await flushPromises()

    expect(mocked.resendCode).toHaveBeenCalled()
    expect(mocked.notifySuccess).toHaveBeenCalledWith('A new code has been sent to your email.')
  })

  it('goes back to the password form', async () => {
    mocked.authStore.twoFactorPending = true

    const wrapper = mount(LoginView)

    const backButton = wrapper.findAll('button').find((b) => b.text() === 'Back to login')
    await backButton!.trigger('click')

    expect(mocked.cancelTwoFactor).toHaveBeenCalled()
  })
})