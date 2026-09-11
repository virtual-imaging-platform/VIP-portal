import { createRouter, createWebHistory } from 'vue-router'
import { getFrontendBase } from '@/utils/path'
import { useAuthStore } from '@/stores/auth.store'

const routerBase = import.meta.env.PROD ? (getFrontendBase() || import.meta.env.BASE_URL || '/') : import.meta.env.BASE_URL

const router = createRouter({
  history: createWebHistory(routerBase),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
    },
    {
      path: '/activate/:id?',
      name: 'activate',
      component: () => import('@/views/ActivateView.vue'),
      meta: { title: 'Activate Account' },
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/DashboardView.vue'),
      meta: { requiresAuth: true, title: 'Dashboard' },
    },
    {
      path: '/files',
      name: 'files',
      component: () => import('@/views/FilesView.vue'),
      meta: { requiresAuth: true, title: 'Files' },
    },
    {
      path: '/publications',
      name: 'publications',
      component: () => import('@/views/PublicationsView.vue'),
      meta: { requiresAuth: true, title: 'Publications' },
    },
    {
      path: '/messages',
      name: 'messages',
      component: () => import('@/views/MessagesView.vue'),
      meta: { requiresAuth: true, title: 'Messages' },
    },
    {
      path: '/applications',
      name: 'applications',
      component: () => import('@/views/ApplicationsView.vue'),
      meta: { requiresAuth: true, title: 'Applications' },
    },
    {
      path: '/applications/:name',
      name: 'application-detail',
      component: () => import('@/views/ApplicationDetailView.vue'),
      meta: { requiresAuth: true, title: 'Application details' },
    },
    {
      path: '/applications/:name/launch/:version?',
      name: 'application-launch',
      component: () => import('@/views/ApplicationLaunchView.vue'),
      meta: { requiresAuth: true, title: 'Launch Application' },
    },
    {
      path: '/applications/create',
      name: 'application-create',
      component: () => import('@/views/CreateApplicationView.vue'),
      meta: { requiresAuth: true, title: 'Create Application' },
    },
    {
      path: '/executions',
      name: 'executions',
      component: () => import('@/views/WorkflowsView.vue'),
      meta: { requiresAuth: true, title: 'Executions' },
    },
    {
      path: '/engines',
      name: 'engines',
      component: () => import('@/views/EnginesView.vue'),
      meta: { requiresAuth: true, requiredRoles: ['Administrator'], title: 'Engines' },
    },
    {
      path: '/workflows/:id',
      name: 'workflow-detail',
      component: () => import('@/views/WorkflowDetailView.vue'),
      meta: { requiresAuth: true, title: 'Workflow Execution' },
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/ProfileView.vue'),
      meta: { requiresAuth: true, title: 'My account' },
    },
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/LandingView.vue'),
      meta: { title: 'Landing' },
    },
  ],
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()
  const requiredRoles = to.meta.requiredRoles as string[]

  if (!auth.initialized) {
    await auth.initialize()
  }

  if (to.name === 'home') {
    return auth.isAuthenticated ? { name: 'dashboard' } : undefined
  }

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login' }
  }

  if (
    !to.meta.requiresAuth &&
    auth.isAuthenticated &&
    ['login', 'register', 'activate'].includes(to.name as string)
  ) {
    return { name: 'dashboard' }
  }

  if (
    to.meta.requiresAuth &&
    auth.isAuthenticated &&
    requiredRoles?.length &&
    !requiredRoles.some(role =>
      auth.user?.level.includes(role)
    )) {
    return { name: 'dashboard' }
  }
})

export default router
