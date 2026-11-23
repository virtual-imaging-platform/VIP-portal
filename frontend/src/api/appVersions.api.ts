import { backendClient } from './client'
import type { AppVersion } from '@/types/appversion.types'
import type { BoutiquesDescriptor } from '@/types/appversion.types'
import type { PrecisePage } from '@/types/application.types'
import type { Tag } from '@/types/tag.types'

export interface BackendAppVersion {
  applicationName: string
  version: string
  descriptor: string | null
  parsedDescriptor: BoutiquesDescriptor | null
  doi: string | null
  visible: boolean
  resources: { name: string; status: boolean; configuration: string }[]
  tags: Tag[]
  settings: object[]
  source: string | null
  note: string | null
}

type BackendAppVersionRaw = Omit<BackendAppVersion, 'parsedDescriptor'>

function parseDescriptor(descriptor: string | null): BoutiquesDescriptor | null {
  if (!descriptor) return null
  try {
    const parsed = JSON.parse(descriptor)
    return parsed !== null && typeof parsed === 'object' ? (parsed as BoutiquesDescriptor) : null
  } catch {
    return null
  }
}

function withParsedDescriptor(version: BackendAppVersionRaw): BackendAppVersion {
  return {
    ...version,
    tags: version.tags ?? [],
    parsedDescriptor: parseDescriptor(version.descriptor),
  }
}

export interface CreateAppVersionPayload {
  applicationName: string
  version: string
  descriptor: string | null
  doi?: string | null
  visible: boolean
  source?: string | null
  note?: string | null
  resources?: { name: string; status: boolean; configuration: string }[]
  tags?: Tag[]
  settings?: Record<string, string>
}

export const appVersionsApi = {

  getAll: (_offset = 0, _quantity = 50) => {
    return backendClient
      .get<PrecisePage<BackendAppVersionRaw>>(
        `/internal/applications/versions`
      )
      .then((r) => ({
        ...r.data,
        data: r.data.data.map(withParsedDescriptor),
      }))
  },
    
  getAllForApplication: (applicationId: string, _offset = 0, _quantity = 50) => {
    return backendClient
      .get<PrecisePage<BackendAppVersionRaw>>(
        `/internal/applications/${encodeURIComponent(applicationId)}/versions`
      )
      .then((r) => ({
        ...r.data,
        data: r.data.data.map(withParsedDescriptor),
      }))
  },

  getByVersion: (applicationId: string, version: string) =>
    backendClient
      .get<BackendAppVersionRaw>(
        `/internal/applications/${encodeURIComponent(applicationId)}/versions/${encodeURIComponent(version)}`,
      )
      .then((r) => withParsedDescriptor(r.data)),

  exists: async (applicationName: string, version: string) => {
    const page = await appVersionsApi.getAllForApplication(applicationName)
    console.log(`Checking existence of version '${version}' for application '${applicationName}' among ${page.total} versions.`)
    console.log('Versions retrieved:', page.data.map(v => v.version))
    return page.data.some((v) => v.version === version)
  },
  
  create: (payload: CreateAppVersionPayload) =>
    backendClient
      .post<AppVersion>(
        `/internal/applications/${encodeURIComponent(payload.applicationName)}/versions`,
        {
          applicationName: payload.applicationName,
          version: payload.version,
          descriptor: payload.descriptor,
          doi: payload.doi ?? null,
          visible: payload.visible,
          source: payload.source ?? null,
          note: payload.note ?? null,
          resources: payload.resources ?? [],
          tags: payload.tags ?? [],
          settings: payload.settings ?? {},
        },
      )
      .then((r) => r.data),

  createOrUpdate: (payload: CreateAppVersionPayload) =>
    backendClient
      .put<AppVersion>(
        `/internal/applications/${encodeURIComponent(payload.applicationName)}/versions/${encodeURIComponent(payload.version)}`,
        {
          applicationName: payload.applicationName,
          version: payload.version,
          descriptor: payload.descriptor,
          doi: payload.doi ?? null,
          visible: payload.visible,
          source: payload.source ?? null,
          note: payload.note ?? null,
          resources: payload.resources ?? [],
          tags: payload.tags ?? [],
          settings: payload.settings ?? {},
        },
      )
      .then((r) => r.data),
}