<template>
  <div class="info-container">
    <v-toolbar color="blue" height="48">
      <template #default>
        <v-tabs v-model="tab">
          <v-tab value="info">thông tin</v-tab>
          <v-tab value="camera">camera</v-tab>
        </v-tabs>
      </template>
      <template #append>
        <v-btn>
          <v-icon icon="fas fa-xmark" />
        </v-btn>
      </template>
    </v-toolbar>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const tab = ref<string>('');
</script>

<style scoped>
.info-container {
  position: absolute;
  height: 280px;
  width: 100%;
  bottom: 0;
  left: 0;
  background-color: #212121;
  z-index: 1000;
  box-shadow: 0 -4px 3px -5px #212121;
}
</style>
