# VIP Portal Frontend

Vue 3 + TypeScript frontend for the VIP Portal.

## Features
- Authentication (login/register)
- Application catalog and version details
- Descriptor-driven launch forms (Boutiques)
- Application/version creation flow

## Stack
- Vue 3 (Composition API)
- TypeScript
- Vite
- Pinia
- Vue Router
- Vitest + Vue Test Utils
- Playwright

## Project Structure
- `src/views`: page-level features
- `src/components/ui`: reusable UI components
- `src/stores`: global state and business orchestration
- `src/api`: backend HTTP clients (`/internal/...`)
- `src/composables`: reusable domain logic
- `src/utils`: pure helpers
- `src/types`: shared TypeScript contracts

## Run Locally

Modify the Backend IP in `.env.developement`

```bash
npm install
npm run dev
```

## Build and Validate
```bash
npm run type-check
npm run build
npm run test:unit:run
```

## Other useful command
```
npm run test:unit:ci # code coverage
```

## Notes
- Dev mode proxies `/internal` requests to the backend (see `vite.config.ts`).
- Production base path is `/new_front/`.
