<template>
  <div id="app-progressive">
    <v-progress-linear
      v-if="commonStore.loading"
      color="green"
      :indeterminate="commonStore.loading"
      :height="8"
    ></v-progress-linear>
  </div>
</template>

<script lang="ts" setup>
import { useCommonStore } from '@/stores/common';

const commonStore = useCommonStore();
</script>
