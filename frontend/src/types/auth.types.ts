export interface LoginCredentials {
  username: string
  password: string
}

export interface RegisterPayload {
  firstName: string
  lastName: string
  email: string
  password: string
  countryCode: string
  institution: string
  comments: string
}

export type VipUserLevel = 'User' | 'Administrator'

export interface VipSession {
  id: string
  email: string
  userlevel: VipUserLevel
  confirmed?: boolean
}

export interface User {
  email: string
}

export interface TwoFactorCodeChallenge {
  twoFactorRequired: boolean
}