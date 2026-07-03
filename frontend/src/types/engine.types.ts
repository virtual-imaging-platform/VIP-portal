export enum EngineStatus {'enabled' , 'disabled'}

export interface Engine {
  name: string
  endpoint: string
  status: EngineStatus
}
