<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import {
  ArrowRight,
  Cpu,
  FlaskConical,
  BarChart3,
  Users,
  BookOpen,
  ExternalLink,
  Calendar,
  ChevronLeft,
  ChevronRight
} from 'lucide-vue-next'

import { getGroupBadgeColor } from '@/utils/groupColor'
import { getFrontendBase } from '@/utils/path'
import AppBadge from '@/components/ui/AppBadge.vue'
import { applicationsApi } from '@/api/applications.api'
import type { BackendApplication } from '@/api/applications.api'
import { publicationsApi } from '@/api/publications.api'
import type { Publication } from '@/types/publication.types'

const stats = [
  { value: '30+', label: 'Applications' },
  { value: '1,500+', label: 'Researchers' },
  { value: '65  +', label: 'Publications' },
  { value: '15+', label: 'Years of experience' },
]

const topApplications = ref<BackendApplication[]>([])
const topPublications = ref<Publication[]>([])

async function loadPublicApplications() {
  try {
    topApplications.value = await applicationsApi.getPublic()
    appPage.value = 0
  } catch (error) {
    console.error('Failed to load public applications:', error)
    topApplications.value = []
  }
}

async function loadPublicPublications() {
  try {
    topPublications.value = await publicationsApi.getPublic()
    pubPage.value = 0
  } catch (error) {
    console.error('Failed to load public publications:', error)
    topPublications.value = []
  }
}

const CAROUSEL_PAGE_SIZE = 9

const appPage = ref(0)
const pubPage = ref(0)

const appPages = computed(() =>
  Array.from({ length: Math.ceil(topApplications.value.length / CAROUSEL_PAGE_SIZE) }, (_, i) =>
    topApplications.value.slice(i * CAROUSEL_PAGE_SIZE, (i + 1) * CAROUSEL_PAGE_SIZE),
  ),
)

const pubPages = computed(() =>
  Array.from({ length: Math.ceil(topPublications.value.length / CAROUSEL_PAGE_SIZE) }, (_, i) =>
    topPublications.value.slice(i * CAROUSEL_PAGE_SIZE, (i + 1) * CAROUSEL_PAGE_SIZE),
  ),
)

const visibleApps = computed(() => appPages.value[appPage.value] ?? [])
const visiblePubs = computed(() => pubPages.value[pubPage.value] ?? [])

function goToAppPage(page: number) {
  appPage.value = Math.min(Math.max(page, 0), Math.max(appPages.value.length - 1, 0))
}

function goToPubPage(page: number) {
  pubPage.value = Math.min(Math.max(page, 0), Math.max(pubPages.value.length - 1, 0))
}

const frontendBase = getFrontendBase()

const team = [
  {
    name: 'Axel Bonnet',
    role: 'Main Developer',
    org: 'CREATIS',
    url: 'https://www.egi.eu/people/axel-bonnet/',
    photo: `${frontendBase}team/axel_bonnet.jpg`,
  },
  {
    name: 'Sorina Pop',
    role: 'Project Manager',
    org: 'CREATIS',
    url: 'https://www.creatis.insa-lyon.fr/site7/fr/users/camarasu',
    photo: `${frontendBase}team/sorina_pop.jpg`,
  },
  {
    name: 'Bertrand Pautet',
    role: 'Research Engineer',
    org: 'CREATIS',
    url: null,
    photo: `${frontendBase}team/photo_bertrand.png`,
  },
  {
    name: 'Guillaume Vinet',
    role: 'Research Engineer',
    org: 'CREATIS',
    url: null,
    photo: `${frontendBase}team/guillaume_vinet.png`,
  },
  {
    name: 'Mayssa Rouissi',
    role: 'Research Engineer',
    org: 'CREATIS',
    url: null,
    photo: `${frontendBase}team/mayssa_rouissi.png`,
  },
  {
    name: 'Hippolyte Blot',
    role: 'Apprentice',
    org: 'CREATIS',
    url: null,
    photo: `${frontendBase}team/hippolyte_blot.jpg`,
  },
]

const formerContributors = [
  {
    name: 'Tristan Glatard',
    role: 'VIP Founder',
    org: 'Concordia University',
    url: 'https://users.encs.concordia.ca/~tglatard/',
    photo: `${frontendBase}team/tristan_glatard.jpg`,
  },
  {
    name: 'Rafael Ferreira da Silva',
    role: 'Senior Research Scientist',
    org: 'Oak Ridge National Laboratory',
    url: 'https://rafaelsilva.com/',
    photo: `${frontendBase}team/rafael_silva.jpg`,
  },
  {
    name: 'Gaël Vila',
    role: 'Post-Doctoral Researcher',
    org: 'ReproVIP',
    url: 'https://www.creatis.insa-lyon.fr/reprovip/',
    photo: `${frontendBase}team/gael_vila.jpg`,
  },
  {
    name: 'Alexandre Cornier',
    role: 'Research Engineer',
    org: 'EGI-ACE & ReproVIP',
    url: 'https://www.egi.eu/project/egi-ace/',
    photo: `${frontendBase}team/alex_cornier.jpg`,
  },
  {
    name: 'Sandesh Patil',
    role: 'Research Engineer',
    org: 'FLI-IAM',
    url: 'https://www.francelifeimaging.fr/',
    photo: `${frontendBase}team/sandesh_patil.jpg`,
  },
  {
    name: 'Ethaniel Billon',
    role: 'Engineer',
    org: 'PEPR Chronicardio',
    url: 'https://pepr-santenum.fr/2023/11/08/chronicardio/',
    photo: `${frontendBase}team/ethaniel_billon.jpg`,
  },
  {
    name: 'Nicolas Georges',
    role: 'Research Engineer',
    org: 'EUCAIM',
    url: 'https://www.eibir.org/projects/eucaim/',
    photo: `${frontendBase}team/nicolas_georges.png`,
  },
  {
    name: 'Gwenaël Ambrosino-Ielpo',
    role: 'Research Engineer',
    org: 'FLI-IAM',
    url: 'https://www.francelifeimaging.fr/',
    photo: `${frontendBase}team/gwenael_ambrosino.jpg`,
  },
]

const steeringCommittee = [
  { name: 'Olivier Beuf', role: 'Director of CREATIS laboratory' },
  { name: 'Hugues Benoit-Cattin', role: 'Deputy Director INSA Lyon, Digital' },
  { name: 'Jérôme Pansanel', role: 'Technical Director at IPHC' },
]

function formatNumber(n: number) {
  return n.toLocaleString('en-US')
}

const heroSection = ref<HTMLElement | null>(null)
const useSolidNavbar = ref(false)

function updateNavbarState() {
  const heroBottom = heroSection.value?.getBoundingClientRect().bottom ?? 0
  // Switch to solid navbar once hero is mostly out of view.
  useSolidNavbar.value = heroBottom <= 96
}

onMounted(() => {
  loadPublicApplications()
  loadPublicPublications()
  updateNavbarState()
  window.addEventListener('scroll', updateNavbarState, { passive: true })
  window.addEventListener('resize', updateNavbarState)
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateNavbarState)
  window.removeEventListener('resize', updateNavbarState)
})
</script>

<template>
  <div class="min-h-screen bg-white font-sans">

    <!-- Navbar -->
    <header
      :class="[
        'fixed inset-x-0 top-0 z-50 border-b backdrop-blur transition-colors duration-300',
        useSolidNavbar
          ? 'border-primary-500/30 bg-linear-to-r from-primary-700/95 to-primary-900/95 shadow-lg shadow-primary-900/20'
          : 'border-white/10 bg-transparent',
      ]"
    >
      <div class="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <div class="flex items-center gap-3">
          <img :src="`${frontendBase}team/vip-logo-without-text.png`" alt="VIP Logo" class="h-8 w-auto rounded-sm" />
          <span class="text-lg font-bold text-white">VIP Portal</span>
        </div>
        <nav class="hidden items-center gap-8 text-sm font-medium text-primary-200 md:flex">
          <a href="#about" class="transition hover:text-white">About</a>
          <a href="#applications" class="transition hover:text-white">Applications</a>
          <a href="#publications" class="transition hover:text-white">Publications</a>
          <a href="#team" class="transition hover:text-white">Team</a>
        </nav>
        <div class="flex items-center gap-3">
          <RouterLink
            to="/login"
            class="rounded-lg px-4 py-2 text-sm font-medium text-primary-100 transition hover:text-white"
          >
            Sign in
          </RouterLink>
          <RouterLink
            to="/register"
            class="rounded-lg bg-white px-4 py-2 text-sm font-semibold text-primary-700 shadow-sm transition hover:bg-primary-50"
          >
            Sign up
          </RouterLink>
        </div>
      </div>
    </header>

    <!-- Hero (same blue than the auth layout) -->
    <section ref="heroSection" class="relative overflow-hidden bg-linear-to-br from-primary-600 via-primary-700 to-primary-900 pt-16">
      <!-- Decorative blobs -->
      <div class="pointer-events-none absolute -left-32 -top-32 h-96 w-96 rounded-full bg-white/5 blur-3xl" />
      <div class="pointer-events-none absolute -bottom-20 right-0 h-80 w-80 rounded-full bg-primary-400/20 blur-3xl" />
      <div class="pointer-events-none absolute right-1/4 top-1/3 h-64 w-64 rounded-full bg-white/5 blur-2xl" />

      <div class="relative mx-auto max-w-7xl px-6 py-24 sm:py-32 lg:py-40">
        <div class="grid items-center gap-16 lg:grid-cols-2">
          <!-- Left -->
          <div class="text-white">
            <h1 class="mt-4 text-5xl font-extrabold leading-tight tracking-tight sm:text-6xl">
              Virtual<br />
              <span class="bg-linear-to-r from-white to-primary-200 bg-clip-text text-transparent">
                Imaging Platform
              </span>
            </h1>
            <p class="mt-6 max-w-xl text-lg leading-relaxed text-primary-100">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.

            </p>
            <p class="mt-3 text-sm text-primary-300">
              Contact: <a href="mailto:vip-support@creatis.insa-lyon.fr" class="underline hover:text-white">vip-support@creatis.insa-lyon.fr</a>
            </p>
            <div class="mt-10 flex flex-wrap gap-4">
              <RouterLink
                to="/register"
                class="inline-flex items-center gap-2 rounded-xl bg-white px-6 py-3 text-sm font-semibold text-primary-700 shadow-lg transition hover:bg-primary-50 hover:shadow-xl"
              >
                Create an account
                <ArrowRight class="h-4 w-4" />
              </RouterLink>
              <RouterLink
                to="/login"
                class="inline-flex items-center gap-2 rounded-xl border border-white/30 bg-white/10 px-6 py-3 text-sm font-semibold text-white backdrop-blur transition hover:bg-white/20"
              >
                Sign in
              </RouterLink>
            </div>
          </div>

          <!-- Right: Stats grid -->
          <div class="grid grid-cols-2 gap-4">
            <div
              v-for="stat in stats"
              :key="stat.label"
              class="rounded-2xl border border-white/10 bg-white/10 p-6 backdrop-blur-sm"
            >
              <p class="text-4xl font-extrabold text-white">{{ stat.value }}</p>
              <p class="mt-1 text-sm text-primary-200">{{ stat.label }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Wave -->
      <div class="relative -mb-px">
        <svg viewBox="0 0 1440 80" xmlns="http://www.w3.org/2000/svg" class="block w-full" preserveAspectRatio="none">
          <path d="M0,40 C360,80 1080,0 1440,40 L1440,80 L0,80 Z" fill="white" />
        </svg>
      </div>
    </section>

    <!-- About -->
    <section id="about" class="bg-white py-24">
      <div class="mx-auto max-w-7xl px-6">
        <div class="mx-auto max-w-3xl text-center">
          <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            A <span class="text-primary-600">worldwide</span> research platform
          </h2>
          <p class="mt-4 text-lg text-gray-500 leading-relaxed">
            VIP leverages the EGI e-infrastructure to deliver an open service to researchers across
            the globe. Trouver d'autres trucs à dire
          </p>
        </div>

        <div class="mt-16 grid gap-8 sm:grid-cols-3">
          <div class="rounded-2xl border border-gray-100 bg-gray-50 p-8 text-center">
            <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-primary-100">
              <Cpu class="h-7 w-7 text-primary-600" />
            </div>
            <h3 class="text-lg font-semibold text-gray-900">High-throughput computing</h3>
            <p class="mt-2 text-sm text-gray-500 leading-relaxed">
              Distribute your workflows across European computing grids with zero manual configuration.
            </p>
          </div>
          <div class="rounded-2xl border border-gray-100 bg-gray-50 p-8 text-center">
            <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-primary-100">
              <FlaskConical class="h-7 w-7 text-primary-600" />
            </div>
            <h3 class="text-lg font-semibold text-gray-900">Reproducibility</h3>
            <p class="mt-2 text-sm text-gray-500 leading-relaxed">
              Application versioning and full execution traceability for open and reproducible science.
            </p>
          </div>
          <div class="rounded-2xl border border-gray-100 bg-gray-50 p-8 text-center">
            <div class="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-xl bg-primary-100">
              <BarChart3 class="h-7 w-7 text-primary-600" />
            </div>
            <h3 class="text-lg font-semibold text-gray-900">Large-scale analysis</h3>
            <p class="mt-2 text-sm text-gray-500 leading-relaxed">
              Thousands of executions performed each year across various fields of medical imaging
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- Top Applications -->
    <section id="applications" class="bg-gray-50 py-24">
      <div class="mx-auto max-w-7xl px-6">
        <div class="flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-end">
          <div>
            <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
              Most used applications
            </h2>
            <p class="mt-2 text-gray-500">
              The VIP community's most used tools for medical image processing.
            </p>
          </div>
          <RouterLink
            to="/applications"
            class="inline-flex items-center gap-2 rounded-lg bg-primary-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-primary-700"
          >
            Browse all applications
            <ArrowRight class="h-4 w-4" />
          </RouterLink>
        </div>

        <div class="mt-10">
          <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
            <div
              v-for="app in visibleApps"
              :key="app.name"
              class="group relative flex h-44 flex-col rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition-all hover:shadow-md hover:border-primary-200"
            >
              <div class="flex items-start gap-4">
                <div class="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-linear-to-br from-primary-600 to-primary-800 text-white text-lg font-bold shadow">
                  {{ app.name.charAt(0) }}
                </div>
                <div class="min-w-0">
                  <div class="flex items-center gap-2">
                    <h3 class="truncate text-base font-bold text-gray-900">{{ app.name }}</h3>
                  </div>
                  <AppBadge :variant="getGroupBadgeColor(app.groups?.[0]?.name ?? 'Uncategorized')" class="mt-1">
                    {{ app.groups?.[0]?.name ?? 'Uncategorized' }}
                  </AppBadge>
                </div>
              </div>

              <p class="mt-4 line-clamp-2 text-sm text-gray-500 leading-relaxed">
                {{ app.citation }}
              </p>
            </div>
          </div>

          <div
            v-if="appPages.length > 1"
            class="mt-6 flex items-center justify-center gap-4"
          >
            <button
              type="button"
              :disabled="appPage === 0"
              class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200 bg-white text-gray-600 shadow-sm transition hover:border-primary-300 hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
              aria-label="Previous applications"
              @click="goToAppPage(appPage - 1)"
            >
              <ChevronLeft class="h-4 w-4" />
            </button>
            <div class="flex items-center gap-2">
              <button
                v-for="(_, index) in appPages"
                :key="index"
                type="button"
                :class="[
                  'h-2.5 rounded-full transition-all',
                  index === appPage ? 'w-6 bg-primary-600' : 'w-2.5 bg-gray-300 hover:bg-gray-400',
                ]"
                :aria-label="`Go to applications page ${index + 1}`"
                @click="goToAppPage(index)"
              />
            </div>
            <button
              type="button"
              :disabled="appPage >= appPages.length - 1"
              class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200 bg-white text-gray-600 shadow-sm transition hover:border-primary-300 hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
              aria-label="Next applications"
              @click="goToAppPage(appPage + 1)"
            >
              <ChevronRight class="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Publications -->
    <section id="publications" class="bg-white py-24">
      <div class="mx-auto max-w-7xl px-6">
        <div class="flex flex-col items-start justify-between gap-4 sm:flex-row sm:items-end">
          <div>
            <h2 class="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
              Latest publications
            </h2>
            <p class="mt-2 text-gray-500">
              Recent works from the community using the VIP platform.
            </p>
          </div>
          <a
            href="https://www.creatis.insa-lyon.fr/vip/news.html"
            target="_blank"
            rel="noopener"
            class="inline-flex items-center gap-2 text-sm font-medium text-primary-600 hover:text-primary-700"
          >
            All publications <ExternalLink class="h-4 w-4" />
          </a>
        </div>

        <div class="mt-10">
          <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
            <a
              v-for="pub in visiblePubs"
              :key="pub.id"
              :href="pub.doi ? `https://doi.org/${pub.doi}` : undefined"
              target="_blank"
              rel="noopener"
              class="group flex h-56 flex-col overflow-hidden rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition-all hover:shadow-md hover:border-primary-200"
            >
              <div class="flex items-start justify-between gap-3">
                <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-primary-50">
                  <BookOpen class="h-5 w-5 text-primary-600" />
                </div>
                <ExternalLink class="h-4 w-4 shrink-0 text-gray-300 transition group-hover:text-primary-400" />
              </div>

              <h3 class="mt-4 line-clamp-3 text-sm font-semibold leading-snug text-gray-900 group-hover:text-primary-700 transition">
                {{ pub.title }}
              </h3>
              <p class="mt-2 text-xs text-gray-400">{{ pub.authors }}</p>

              <div class="mt-4 flex flex-wrap items-center gap-2">
                <span v-if="pub.typeName" class="inline-flex items-center gap-1 rounded-full bg-primary-50 px-2.5 py-1 text-xs font-medium text-primary-700">
                  <BookOpen class="h-3 w-3" />
                  {{ pub.typeName }}
                </span>
                <span v-if="pub.date" class="inline-flex items-center gap-1 rounded-full bg-gray-100 px-2.5 py-1 text-xs text-gray-500">
                  <Calendar class="h-3 w-3" />
                  {{ pub.date }}
                </span>
              </div>
            </a>
          </div>

          <div
            v-if="pubPages.length > 1"
            class="mt-6 flex items-center justify-center gap-4"
          >
            <button
              type="button"
              :disabled="pubPage === 0"
              class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200 bg-white text-gray-600 shadow-sm transition hover:border-primary-300 hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
              aria-label="Previous publications"
              @click="goToPubPage(pubPage - 1)"
            >
              <ChevronLeft class="h-4 w-4" />
            </button>
            <div class="flex items-center gap-2">
              <button
                v-for="(_, index) in pubPages"
                :key="index"
                type="button"
                :class="[
                  'h-2.5 rounded-full transition-all',
                  index === pubPage ? 'w-6 bg-primary-600' : 'w-2.5 bg-gray-300 hover:bg-gray-400',
                ]"
                :aria-label="`Go to publications page ${index + 1}`"
                @click="goToPubPage(index)"
              />
            </div>
            <button
              type="button"
              :disabled="pubPage >= pubPages.length - 1"
              class="flex h-9 w-9 items-center justify-center rounded-full border border-gray-200 bg-white text-gray-600 shadow-sm transition hover:border-primary-300 hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
              aria-label="Next publications"
              @click="goToPubPage(pubPage + 1)"
            >
              <ChevronRight class="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Team -->
    <section id="team" class="bg-gray-50 py-24">
      <div class="mx-auto max-w-7xl px-6">
        <!-- Current team -->
        <div class="text-center">
          <div class="inline-flex items-center gap-2 rounded-full bg-primary-100 px-4 py-1.5 text-sm font-medium text-primary-700">
            <Users class="h-4 w-4" />
            Current team
          </div>
          <h2 class="mt-4 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            The people behind VIP
          </h2>
          <p class="mt-3 text-gray-500">
            A research and engineering team based primarily at the CREATIS laboratory, INSA Lyon.
          </p>
        </div>

        <div class="mt-12 grid gap-6 sm:grid-cols-2">
          <div
            v-for="member in team"
            :key="member.name"
            class="group rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition-all hover:shadow-md hover:border-primary-200"
          >
            <div class="flex items-center gap-4">
              <img
                :src="member.photo"
                :alt="member.name"
                class="h-16 w-16 rounded-full object-cover ring-2 ring-gray-100 ring-offset-2 transition group-hover:ring-primary-200"
                loading="lazy"
              />
              <div>
                <h3 class="font-bold text-gray-900">{{ member.name }}</h3>
                <p class="text-sm text-gray-500">{{ member.role }}</p>
                <span class="mt-1 inline-block rounded-full bg-primary-50 px-2 py-0.5 text-xs font-medium text-primary-600">
                  {{ member.org }}
                </span>
              </div>
            </div>
            <a
              v-if="member.url"
              :href="member.url"
              target="_blank"
              rel="noopener"
              class="mt-4 inline-flex items-center gap-1 text-xs font-medium text-primary-600 hover:text-primary-700"
            >
              More info <ExternalLink class="h-3 w-3" />
            </a>
          </div>
        </div>

        <!-- Former contributors -->
        <div class="mt-20 text-center">
          <div class="inline-flex items-center gap-2 rounded-full bg-gray-200 px-4 py-1.5 text-sm font-medium text-gray-600">
            Former contributors
          </div>
          <h2 class="mt-4 text-2xl font-bold text-gray-800">
            Those who shaped the project
          </h2>
        </div>

        <div class="mt-10 grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
          <div
            v-for="member in formerContributors"
            :key="member.name"
            class="rounded-2xl border border-gray-100 bg-white p-5 text-center shadow-sm transition-all hover:shadow-md"
          >
            <img
              :src="member.photo"
              :alt="member.name"
              class="mx-auto h-16 w-16 rounded-full object-cover ring-2 ring-gray-100"
              loading="lazy"
            />
            <h3 class="mt-3 text-sm font-bold text-gray-900">{{ member.name }}</h3>
            <p class="mt-0.5 text-xs text-gray-500">{{ member.role }}</p>
            <span class="mt-1 inline-block rounded-full bg-gray-100 px-2 py-0.5 text-xs text-gray-500">
              {{ member.org }}
            </span>
            <a
              v-if="member.url"
              :href="member.url"
              target="_blank"
              rel="noopener"
              class="mt-3 flex items-center justify-center gap-1 text-xs font-medium text-primary-600 hover:text-primary-700"
            >
              More info <ExternalLink class="h-3 w-3" />
            </a>
          </div>
        </div>

        <!-- Steering committee -->
        <div class="mt-20">
          <div class="text-center">
            <div class="inline-flex items-center gap-2 rounded-full bg-gray-200 px-4 py-1.5 text-sm font-medium text-gray-600">
              Steering committee
            </div>
            <h2 class="mt-4 text-2xl font-bold text-gray-800">
              Platform governance
            </h2>
          </div>
          <div class="mt-8 flex flex-wrap justify-center gap-4">
            <div
              v-for="member in steeringCommittee"
              :key="member.name"
              class="rounded-2xl border border-gray-100 bg-white px-8 py-5 text-center shadow-sm"
            >
              <div class="mx-auto mb-3 flex h-12 w-12 items-center justify-center rounded-full bg-linear-to-br from-primary-600 to-primary-800 text-lg font-bold text-white shadow">
                {{ member.name.charAt(0) }}
              </div>
              <h3 class="text-sm font-bold text-gray-900">{{ member.name }}</h3>
              <p class="mt-0.5 text-xs text-gray-500">{{ member.role }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-linear-to-br from-primary-800 to-primary-950 text-white">
      <div class="mx-auto max-w-7xl px-6 py-12">
        <div class="flex flex-col items-center gap-6 sm:flex-row sm:justify-between">
          <div class="flex items-center gap-3">
            <img :src="`${frontendBase}team/vip-logo-without-text.png`" alt="VIP Logo" class="h-8 w-auto rounded-sm" />
            <div>
              <p class="font-semibold">VIP Portal</p>
              <p class="text-xs text-primary-300">Virtual Imaging Platform</p>
            </div>
          </div>
          <div class="flex flex-wrap justify-center gap-6 text-sm text-primary-300">
            <a href="https://github.com/virtual-imaging-platform/VIP-portal/wiki" target="_blank" rel="noopener" class="transition hover:text-white">Documentation</a>
            <a href="https://vip.creatis.insa-lyon.fr/documentation/privacypolicy.html" target="_blank" rel="noopener" class="transition hover:text-white">Privacy Policy</a>
            <a href="https://vip.creatis.insa-lyon.fr/documentation/terms.html" target="_blank" rel="noopener" class="transition hover:text-white">Terms of Use</a>
            <a href="mailto:vip-support@creatis.insa-lyon.fr" class="transition hover:text-white">Contact</a>
          </div>
        </div>
        <div class="mt-8 border-t border-white/10 pt-6 text-center text-xs text-primary-400">
          © {{ new Date().getFullYear() }} Virtual Imaging Platform - CREATIS, INSA Lyon
        </div>
      </div>
    </footer>

  </div>
</template>
