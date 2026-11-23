export function getFrontendBase(): string {
  if (typeof window === 'undefined') return '/'
  const script = document.querySelector<HTMLScriptElement>('script[type="module"][src]')
  if (!script) return '/'
  const parts = new URL(script.src).pathname.split('/')
  parts.pop()
  parts.pop()
  return parts.join('/') + '/'
}

export function getBackendBase(): string {
  const base = getFrontendBase()
  if (base === '/') return '/'
  const parts = base.split('/').filter(Boolean)
  parts.pop()
  return '/' + parts.join('/') + (parts.length > 0 ? '/' : '')
}
