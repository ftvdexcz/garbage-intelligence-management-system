<template>
  <div id="alert-message">
    <v-slide-x-reverse-transition>
      <v-alert
        v-if="commonStore.alert"
        :text="commonStore.alertMsg"
        :type="commonStore.alertType"
      ></v-alert>
    </v-slide-x-reverse-transition>
  </div>
</template>

<script lang="ts" setup>
import { useCommonStore } from '@/stores/common';

const commonStore = useCommonStore();
</script>
