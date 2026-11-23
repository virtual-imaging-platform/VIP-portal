export type WorkflowStatus =
  | 'Running'
  | 'Completed'
  | 'Failed'
  | 'Killed'
  | 'Cleaned'
  | 'Queued'
  | 'Unknown'

export interface Workflow {
  id: string
  applicationName: string
  applicationVersion: string
  workflowName: string
  status: WorkflowStatus
  inputs: Record<string, {
    values?: string[]
    interval?: number[]
  }>
  outputs: Record<string, string>
  userId: string
  userFullName?: string
  startDate: string
  endDate?: string
}

export interface WorkflowInputPayload {
  values?: string[]
  interval?: number[]
}

export interface WorkflowLaunchPayload {
  applicationName: string
  applicationVersion: string
  workflowName: string
  inputs: Record<string, WorkflowInputPayload>
}

export interface Task {
  id: string
  invocationID: number
  creationDate: string
  status: string
  exitCode: number
  siteName: string
  nodeName: string
  command: string
  fileName: string
  jobID: number
}

export type JobLogType = 'app-stdout' | 'app-stderr' | 'stdout' | 'stderr' | 'script'

export const JOB_LOG_TYPES: { value: JobLogType; label: string }[] = [
  { value: 'app-stdout', label: 'Application Output' },
  { value: 'app-stderr', label: 'Application Error' },
  { value: 'stdout', label: 'Output' },
  { value: 'stderr', label: 'Error' },
  { value: 'script', label: 'Script' },
]

export type JobState =
  | 'Submitted'
  | 'Queued'
  | 'Running'
  | 'Completed'
  | 'Failed'
  | 'Cancelled'
  | 'Stalled'

export const JOB_STATE_ORDER: JobState[] = [
  'Submitted',
  'Queued',
  'Running',
  'Completed',
  'Failed',
  'Cancelled',
  'Stalled',
]

export const JOB_STATE_COLORS: Record<JobState, string> = {
  Submitted: '#CC9933',
  Queued: '#DBA400',
  Running: '#8CC653',
  Completed: '#287fd6',
  Failed: '#d64949',
  Cancelled: '#FF8575',
  Stalled: '#1A767F',
}

export const TASK_STATUS_TO_STATE: Record<string, JobState> = {
  SUCCESSFULLY_SUBMITTED: 'Submitted',
  QUEUED: 'Queued',
  RUNNING: 'Running',
  KILL: 'Running',
  KILL_REPLICA: 'Running',
  REPLICATE: 'Running',
  REPLICATING: 'Running',
  REPLICATED: 'Running',
  RESCHEDULE: 'Running',
  COMPLETED: 'Completed',
  ERROR: 'Failed',
  UNHOLD_ERROR: 'Failed',
  ERROR_HELD: 'Failed',
  ERROR_FINISHING: 'Failed',
  ERROR_RESUBMITTING: 'Failed',
  CANCELLED: 'Cancelled',
  CANCELLED_REPLICA: 'Cancelled',
  DELETED: 'Cancelled',
  DELETED_REPLICA: 'Cancelled',
  STALLED: 'Stalled',
  UNHOLD_STALLED: 'Stalled',
  STALLED_HELD: 'Stalled',
  STALLED_FINISHING: 'Stalled',
  STALLED_RESUBMITTING: 'Stalled',
}

export interface WorkflowListParams {
  offset?: number
  quantity?: number
  search?: string
  application?: string
  status?: string
  startDate?: string
  endDate?: string
  tag?: string
}
