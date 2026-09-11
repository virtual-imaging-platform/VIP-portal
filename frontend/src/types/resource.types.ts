import type { Group } from '@/types/group.types'

export const ResourceTypeList = ['LOCAL', 'BATCH', 'KUBERNETES', 'DIRAC']

export type ResourceType = typeof ResourceTypeList[number]

export interface Resource {
  name: string
  status: boolean
  type: ResourceType
  configuration: string
  engines: string[]
  groups: Group[]
}
