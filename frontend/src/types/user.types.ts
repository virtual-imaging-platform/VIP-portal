import type { Group } from './group.types'

export interface UserSuggestion {
  id: string
  email: string
  firstName?: string
  lastName?: string

  confirmed?: boolean
  accountLocked?: boolean
  institution?: string
  registration?: number
  lastLogin?: number
  level?: string
  maxRunningSimulations?: number
  countryCode?: string
  termsOfUse?: number
  lastUpdatePublications?: number
  groups?: Group[]
}