import type { Group } from './group.types'

export interface ProfileUser {
  id: string
  firstName: string
  lastName: string
  email: string
  institution: string
  countryCode: string
  maxRunningSimulations: number
  level: string
  termsOfUse: string | null
  lastUpdatePublications: string | null
  welcomeDismissed: string | null
  groups: Group[]
  groupsMap?: Record<string, string>
  folder?: string
}

export interface ProfileUpdatePayload {
  id: string
  institution: string
  countryCode: string
  groups: Group[]
}
