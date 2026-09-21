export const EngineStatusList = ['enabled', 'disabled']

export type EngineStatus = typeof EngineStatusList[number]

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
