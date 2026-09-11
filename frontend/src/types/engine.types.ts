export type EngineStatus = 'enabled' | 'disabled'

export interface EngineListParams {
  offset?: number
  quantity?: number
  name?: string
  status?: string
  endpoint?: string
  resource?: string
}

export interface Engine {
  name: string
  endpoint: string
  status: EngineStatus
}
