export type EngineStatus = 'enabled' | 'disabled'

export interface EngineListParams {
  offset?: number
  quantity?: number
  resource?: string
  status?: string
  endpoint?: string
}

export interface Engine {
  name: string
  endpoint: string
  status: EngineStatus
}
