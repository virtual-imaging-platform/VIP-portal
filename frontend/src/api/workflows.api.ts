import { backendClient } from './client'
import type { PrecisePage } from '@/types/application.types'
import type { Workflow, WorkflowLaunchPayload, WorkflowListParams, JobLogType } from '@/types/workflow.types'
import type { Task } from '@/types/workflow.types'

const enc = (s: string) => encodeURIComponent(s)

export const workflowsApi = {
  list: (params?: WorkflowListParams) =>
    backendClient
      .get<PrecisePage<Workflow>>('/internal/workflows', { params })
      .then((r) => r.data),

  get: (wid: string) =>
    backendClient
      .get<Workflow>(`/internal/workflows/${enc(wid)}`)
      .then((r) => r.data),

  launch: (payload: WorkflowLaunchPayload) =>
    backendClient
      .post<Workflow>('/internal/workflows', payload)
      .then((r) => r.data),

  kill: (wid: string) =>
    backendClient
      .post(`/internal/workflows/${enc(wid)}/kill`)
      .then((r) => r.data),

  clean: (wid: string, deleteFiles = true) =>
    backendClient
      .post(`/internal/workflows/${enc(wid)}/clean`, null, {
        params: { deleteFiles },
      })
      .then((r) => r.data),

  readStdout: (wid: string) =>
    backendClient
      .get<string>(`/internal/workflows/${enc(wid)}/stdout`)
      .then((r) => r.data),

  readStderr: (wid: string) =>
    backendClient
      .get<string>(`/internal/workflows/${enc(wid)}/stderr`)
      .then((r) => r.data),

  listJobs: (wid: string) =>
    backendClient
      .get<Task[]>(`/internal/workflows/${enc(wid)}/jobs`)
      .then((r) => r.data),

  readJobLog: (wid: string, invocationId: number, type: JobLogType) =>
    backendClient
      .get<string>(`/internal/workflows/${enc(wid)}/jobs/${invocationId}/logs/${type}`)
      .then((r) => r.data),
}
