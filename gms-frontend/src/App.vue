<template>
  <v-app>
    <alert-message />
    <router-view />
    <app-progressive />
  </v-app>
</template>

<script setup lang="ts">
import AlertMessage from '@/components/common/AlertMessage.vue';
import AppProgressive from '@/components/common/AppProgressive.vue';
import { onBeforeMount } from 'vue';
import * as authService from '@/services/auth.service';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

onBeforeMount(async () => {
  const response = await authService.getMe();
  if (response.code == 200) {
    authStore.setUser(response.data);
  }
});
</script>
