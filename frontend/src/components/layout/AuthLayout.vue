<script setup lang="ts"></script>

<template>
  <div class="flex min-h-screen">
    <div
      class="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 items-center justify-center p-12"
    >
      <div class="max-w-lg text-white">
        <router-link to="/" class="flex items-center gap-3 mb-8 hover:opacity-80 transition-opacity">
          <div
            class="flex h-14 w-14 items-center justify-center rounded-xl bg-white backdrop-blur-sm text-white font-bold text-lg"
          >
            <img
              src="@/assets/vip-logo-without-text.png"
              alt="VIP Logo"
              class="h-12 w-auto rounded-sm object-cover"
            />
          </div>
          <span class="text-2xl font-bold">Virtual Imaging Platform</span>
        </router-link>
        <h2 class="text-4xl font-bold leading-tight mb-6">
          Lorem ipsum dolor, sit amet consectetur adipisicing elit
        </h2>
        <p class="text-lg text-primary-100 leading-relaxed">
          Lorem ipsum dolor, sit amet consectetur adipisicing elit. Dolores quisquam, laudantium
          obcaecati quos fugit id! Dolorum consectetur ad debitis nihil vitae aliquam repudiandae
          ratione aspernatur voluptates, dolor voluptate quod reprehenderit.
        </p>
        <div class="mt-10 grid grid-cols-3 gap-6">
          <div>
            <p class="text-3xl font-bold">50+</p>
            <p class="text-sm text-primary-200">Applications</p>
          </div>
          <div>
            <p class="text-3xl font-bold">2k+</p>
            <p class="text-sm text-primary-200">Chercheurs</p>
          </div>
          <div>
            <p class="text-3xl font-bold">100k+</p>
            <p class="text-sm text-primary-200">Exécutions</p>
          </div>
        </div>
      </div>
    </div>

    <div class="flex w-full lg:w-1/2 items-center justify-center p-6 sm:p-12">
      <div class="w-full max-w-md">
        <slot />
      </div>
    </div>
  </div>
</template>
