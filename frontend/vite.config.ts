import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {

  const env = loadEnv(mode, process.cwd(), '')
  const backendUrl = env.VITE_BACKEND_URL || 'http://localhost:8080'
  const base = mode === 'production' ? './' : '/'

  return {
    base,
    plugins: [
      vue(),
      vueJsx(),
      vueDevTools(),
      tailwindcss(),
    ],
    server: {
        proxy: {
          '/internal': {
            target: backendUrl,
            changeOrigin: true,
            secure: false,
          },
        },
      },
      resolve: {
        alias: {
          '@': fileURLToPath(new URL('./src', import.meta.url)),
        },
      },
  }
})
